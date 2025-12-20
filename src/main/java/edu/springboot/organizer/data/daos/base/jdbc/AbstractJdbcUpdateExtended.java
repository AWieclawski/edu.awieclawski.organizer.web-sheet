package edu.springboot.organizer.data.daos.base.jdbc;

import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.utils.ReflectionUtils;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.metadata.TableMetaDataProvider;
import org.springframework.jdbc.core.metadata.TableParameterMetaData;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

abstract class AbstractJdbcUpdateExtended<S extends BaseEntity, T extends BaseDto> extends AbstractJdbcExtended<S, T> {

    protected TableMetaDataProvider metaDataProviderExt;

    protected String primaryKeyColumnName;

    protected String entityId;

    private final Predicate<String> isPrimaryKey = column -> primaryKeyColumnName != null && primaryKeyColumnName.equals(column);

    protected int[][] doExecuteBatchExt(List<T> dtos) {
        isBatch = true;
        checkCompiledExt();
        return getJdbcTemplate().batchUpdate(sqlStringExt, dtos, batchSize,
                (ps, dto) -> {
                    Map<String, Object> entityParameters = rowMapper.toMap(dto);
                    Object idObjectValue = entityParameters.get(primaryKeyColumnName);
                    if (idObjectValue != null) {
                        this.entityId = (String) idObjectValue;
                        entityParameters.remove(BaseEntity.BaseConst.ID.getColumn());
                        List<Object> values = matchInParameterValuesWithUpdateColumns(entityParameters);
                        logCompiled();
                        setParameterValuesList(ps, values, intColumnTypes);
                        // assign Primary Key value with last parameter index in 'WHERE' clause
                        ps.setString(entityParameters.size() + 1, entityId);
                    } else {
                        logger.warn("No Primary Key value found - " + getTableName() + "{} record update skipped");
                    }
                });
    }

    protected int doExecuteExt(Map<String, Object> args) {
        isBatch = false;
        checkCompiledExt();
        List<Object> values = matchInParameterValuesWithUpdateColumns(args);
        return getJdbcTemplate().update(this.sqlStringExt, values.toArray(), this.intColumnTypes);
    }

    protected AbstractJdbcUpdateExtended(DataSource dataSource) {
        super(dataSource);
    }

    protected AbstractJdbcUpdateExtended(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    protected void setEntityId(@Nullable String entityId) {
        checkIfConfigurationModificationIsAllowed();
        this.entityId = entityId;
    }

    protected void setPrimaryKeyColumnName(@Nullable String primaryKeyColumnName) {
        checkIfConfigurationModificationIsAllowed();
        this.primaryKeyColumnName = primaryKeyColumnName;
    }

    @Override
    protected void compileInternalExt() {
        DataSource dataSource = getJdbcTemplate().getDataSource();
        Assert.state(dataSource != null, "No DataSource set");
        if (tableMetaDataContextExt == null) {
            this.tableMetaDataContextExt = getTableMetaDataContext();
            Assert.state(tableMetaDataContextExt != null, "No table data context set!");
            this.tableMetaDataContextExt.processMetaData(dataSource, getColumnNames(), getGeneratedKeyNames());
            this.metaDataProviderExt = getTableMetaDataProvider();
            this.tableColumnsExt = tableMetaDataContextExt.getTableColumns();
            // Remove ID column
            this.tableColumnsExt.removeIf(isPrimaryKey);
            this.sqlStringExt = createUpdateString(getGeneratedKeyNames());
            this.intColumnTypes = createUpdateTypes();
            onCompileInternal();
        }
    }

    TableMetaDataProvider getTableMetaDataProvider() {
        if (tableMetaDataContextExt != null) {
            return ReflectionUtils.getFieldValue(this.tableMetaDataContextExt, "metaDataProvider", true);
        }
        throw new InvalidDataAccessApiUsageException("TableMetaDataProvider instance is required!");
    }

    private List<Object> matchInParameterValuesWithUpdateColumns(Map<String, ?> inParameters) {
        List<Object> values = new ArrayList<>(inParameters.size());
        for (String column : this.tableColumnsExt) {
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

    private int[] createUpdateTypes() {
        int[] types = new int[this.tableColumnsExt.size()];
        List<TableParameterMetaData> parameters = metaDataProviderExt.getTableParameterMetaData();
        Map<String, TableParameterMetaData> parameterMap = CollectionUtils.newLinkedHashMap(parameters.size());
        for (TableParameterMetaData tpmd : parameters) {
            parameterMap.put(tpmd.getParameterName().toUpperCase(), tpmd);
        }
        // remove Primary Key column
        parameterMap.remove(primaryKeyColumnName.toUpperCase());
        int typeIndx = 0;
        for (String column : this.tableColumnsExt) {
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

    private String createUpdateString(String... generatedKeyNames) {
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
        for (String columnName : this.tableColumnsExt) {
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

}
