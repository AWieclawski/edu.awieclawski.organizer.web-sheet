package edu.springboot.organizer.data.repositories.handlers;

import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.utils.ReflectionUtils;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.metadata.TableMetaDataContext;
import org.springframework.jdbc.core.metadata.TableMetaDataProvider;
import org.springframework.jdbc.core.metadata.TableParameterMetaData;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@Slf4j
public class SimpleJdbcUpdate<S extends BaseEntity, T extends BaseDto> extends SimpleJdbcInsert {

    protected TableMetaDataContext tableMetaDataContextUpd;

    protected TableMetaDataProvider metaDataProviderUpd;

    protected String entityId;

    protected String updateStringUpd;

    protected int[] updateTypes;

    protected List<String> tableColumnsUpd;

    protected String primaryKeyColumnName;

    protected Integer batchSize;

    protected BaseRowMapper<S, T> rowMapper;

    private final Predicate<String> isId = column -> primaryKeyColumnName != null && primaryKeyColumnName.equals(column);

    private Boolean isBatch;

    public SimpleJdbcUpdate(DataSource dataSource) {
        super(dataSource);
    }

    public SimpleJdbcUpdate(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected void compileInternal() {
        if (logger.isDebugEnabled()) {
            logger.debug("Start overridden compile internal process to skip insert procedures");
        }
        DataSource dataSource = getJdbcTemplate().getDataSource();
        Assert.state(dataSource != null, "No DataSource set");
        this.tableMetaDataContextUpd = ReflectionUtils.getFieldValue(this, "tableMetaDataContext", true);
        Assert.state(tableMetaDataContextUpd != null, "No table data context set");
        this.tableMetaDataContextUpd.processMetaData(dataSource, getColumnNames(), getGeneratedKeyNames());
        onCompileInternal();
    }

    public SimpleJdbcUpdate<S, T> withEntityId(String entityId) {
        setEntityId(entityId);
        return this;
    }

    public SimpleJdbcUpdate<S, T> withIdColumnName(String idColumnName) {
        setPrimaryKeyColumnName(idColumnName);
        return this;
    }

    public SimpleJdbcUpdate<S, T> withBatchSize(Integer batchSize) {
        setBatchSize(batchSize);
        return this;
    }

    public SimpleJdbcUpdate<S, T> withRawMapper(BaseRowMapper<S, T> rowMapper) {
        setRowMapper(rowMapper);
        return this;
    }

    protected void setEntityId(@Nullable String entityId) {
        checkIfConfigurationModificationIsAllowed();
        this.entityId = entityId;
    }

    protected void setPrimaryKeyColumnName(@Nullable String primaryKeyColumnName) {
        checkIfConfigurationModificationIsAllowed();
        this.primaryKeyColumnName = primaryKeyColumnName;
    }

    public List<String> getTableColumns() {
        return this.tableColumnsUpd;
    }

    public int executeUpdate(Map<String, Object> args) {
        isBatch = false;
        checkCompiled();
        this.metaDataProviderUpd = ReflectionUtils.getFieldValue(tableMetaDataContextUpd, "metaDataProvider", true);
        this.tableColumnsUpd = tableMetaDataContextUpd.getTableColumns();
        // remove id column
        this.tableColumnsUpd.removeIf(isId);
        List<Object> values = matchInParameterValuesWithUpdateColumns(args);
        this.updateTypes = createUpdateTypes();
        compileInternalUpd();
        if (log.isDebugEnabled()) {
            log.debug("Sql used for list update: [{}] with values: {}", this.updateStringUpd, values);
        }
        return getJdbcTemplate().update(this.updateStringUpd, values.toArray(), this.updateTypes);
    }


    /**
     * Source: https://medium.com/@AlexanderObregon/bulk-insert-optimization-with-spring-boot-and-jdbc-batching-57dd031ecad8
     *
     * @param dtos
     * @return
     */
    public int[][] executeBatchUpdate(List<T> dtos) {
        isBatch = true;
        checkCompiled();
        this.metaDataProviderUpd = ReflectionUtils.getFieldValue(tableMetaDataContextUpd, "metaDataProvider", true);
        this.tableColumnsUpd = tableMetaDataContextUpd.getTableColumns();
        // remove Primary Key column
        this.tableColumnsUpd.removeIf(isId);
        this.updateTypes = createUpdateTypes();
        compileInternalUpd();
        return getJdbcTemplate().batchUpdate(updateStringUpd, dtos, batchSize,
                (ps, dto) -> {
                    Map<String, Object> entityParameters = rowMapper.toMap(dto);
                    Object idObjectValue = entityParameters.get(primaryKeyColumnName);
                    if (idObjectValue != null) {
                        this.entityId = (String) idObjectValue;
                        entityParameters.remove(BaseEntity.BaseConst.ID.getColumn());
                        List<Object> values = matchInParameterValuesWithUpdateColumns(entityParameters);
                        if (log.isDebugEnabled()) {
                            log.debug("Sql used for list update: [{}] with values: {} where Primary Key: [{}]", this.updateStringUpd, values, entityId);
                        }
                        setParameterValues(ps, values, updateTypes);
                        // assign Primary Key value with last parameter index in 'WHERE' clause
                        ps.setString(entityParameters.size() + 1, entityId);
                    } else {
                        log.warn("No Primary Key value found - {} record update skipped", getTableName());
                    }
                });
    }

    protected void setBatchSize(@Nullable Integer batchSize) {
        checkIfConfigurationModificationIsAllowed();
        this.batchSize = batchSize;
    }

    protected void setRowMapper(@Nullable BaseRowMapper<S, T> rowMapper) {
        checkIfConfigurationModificationIsAllowed();
        this.rowMapper = rowMapper;
    }

    protected void compileInternalUpd() {
        this.updateStringUpd = createUpdateString(getGeneratedKeyNames());
        if (log.isDebugEnabled()) {
            log.debug("Compiled update object: update SQL string is [{}]", this.updateStringUpd);
        }
    }

    protected String createUpdateString(String... generatedKeyNames) {
        if (isBatch != null && !isBatch && entityId == null) {
            String message = "Unable to update columns for table '" + getTableName()
                    + "' if no entity id.";
            throw new InvalidDataAccessApiUsageException(message);
        }

        Set<String> keys = new LinkedHashSet<>(generatedKeyNames.length);
        for (String key : generatedKeyNames) {
            keys.add(key.toUpperCase());
        }
        StringBuilder updateStatement = new StringBuilder();
        updateStatement.append("UPDATE ");
        if (getSchemaName() != null) {
            updateStatement.append(getSchemaName());
            updateStatement.append('.');
        }
        updateStatement.append(getTableName());
        updateStatement.append(" SET ");
        int columnCount = 0;
        for (String columnName : getTableColumns()) {
            if (!keys.contains(columnName.toUpperCase())) {
                columnCount++;
                if (columnCount > 1) {
                    updateStatement.append(", ");
                }
                updateStatement.append(columnName).append(" = ?");
            }
        }
        if (isBatch != null && isBatch) {
            updateStatement.append(" WHERE ID = ?");
        } else {
            updateStatement.append(" WHERE ID = '").append(entityId).append("'");
        }
        if (columnCount < 1) {
            String message = "Unable to locate columns for table '" + getTableName()
                    + "' so an insert statement can't be generated.";
            throw new InvalidDataAccessApiUsageException(message);
        }

        return updateStatement.toString();
    }

    protected List<Object> matchInParameterValuesWithUpdateColumns(Map<String, ?> inParameters) {
        List<Object> values = new ArrayList<>(inParameters.size());
        for (String column : this.tableColumnsUpd) {
            Object value = inParameters.get(column);
            if (value == null) {
                value = inParameters.get(column.toLowerCase());
                if (value == null) {
                    for (Map.Entry<String, ?> entry : inParameters.entrySet()) {
                        if (column.equalsIgnoreCase(entry.getKey())) {
                            value = entry.getValue();
                            break;
                        }
                    }
                }
            }
            values.add(value);
        }
        return values;
    }

    protected int[] createUpdateTypes() {
        int[] types = new int[getTableColumns().size()];
        List<TableParameterMetaData> parameters = metaDataProviderUpd.getTableParameterMetaData();
        Map<String, TableParameterMetaData> parameterMap = CollectionUtils.newLinkedHashMap(parameters.size());
        for (TableParameterMetaData tpmd : parameters) {
            parameterMap.put(tpmd.getParameterName().toUpperCase(), tpmd);
        }
        // remove Primary Key column
        parameterMap.remove(primaryKeyColumnName.toUpperCase());
        int typeIndx = 0;
        for (String column : getTableColumns()) {
            if (column == null) {
                types[typeIndx] = SqlTypeValue.TYPE_UNKNOWN;
            } else {
                TableParameterMetaData tpmd = parameterMap.get(column.toUpperCase());
                if (tpmd != null) {
                    types[typeIndx] = tpmd.getSqlType();
                } else {
                    types[typeIndx] = SqlTypeValue.TYPE_UNKNOWN;
                }
            }
            typeIndx++;
        }
        return types;
    }

    /**
     * Copy of: org.springframework.jdbc.core.simple.AbstractJdbcInsert#setParameterValues(java.sql.PreparedStatement, java.util.List, int...)
     *
     * @param preparedStatement
     * @param values
     * @param columnTypes
     * @throws SQLException
     */
    private void setParameterValues(PreparedStatement preparedStatement, List<?> values, @Nullable int... columnTypes)
            throws SQLException {
        int colIndex = 0;
        for (Object value : values) {
            colIndex++;
            if (columnTypes == null || colIndex > columnTypes.length) {
                StatementCreatorUtils.setParameterValue(preparedStatement, colIndex, SqlTypeValue.TYPE_UNKNOWN, value);
            } else {
                StatementCreatorUtils.setParameterValue(preparedStatement, colIndex, columnTypes[colIndex - 1], value);
            }
        }
    }

}
