package edu.springboot.organizer.data.daos.base.jdbc;

import edu.springboot.organizer.data.exceptions.NotImplementedException;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.utils.ReflectionUtils;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.metadata.TableMetaDataContext;
import org.springframework.jdbc.core.simple.AbstractJdbcInsert;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

abstract class AbstractJdbcExtended<S extends BaseEntity, T extends BaseDto> extends AbstractJdbcInsert {

    TableMetaDataContext tableMetaDataContextExt;

    List<String> tableColumnsExt;

    int[] intColumnTypes;

    String sqlStringExt;

    Integer batchSize;

    BaseRowMapper<S, T> rowMapper;

    Boolean isBatch;

    protected int[][] doExecuteBatchExt(List<T> dtos) {
        isBatch = true;
        checkCompiledExt();
        return getJdbcTemplate().batchUpdate(sqlStringExt, dtos, batchSize,
                // ParameterizedPreparedStatementSetter
                (ps, dto) -> {
                    Map<String, Object> entityParameters = rowMapper.toMap(dto);
                    List<Object> values = matchInParameterValuesWithInsertColumns(entityParameters);
                    setParameterValuesList(ps, values, intColumnTypes);
                });
    }

    protected int doExecuteExt(Map<String, Object> args) {
        throw new NotImplementedException("Not implemented yet!");
    }

    final synchronized void compileExt() throws InvalidDataAccessApiUsageException {
        if (!isCompiled()) {
            if (getTableName() == null) {
                throw new InvalidDataAccessApiUsageException("Table name is required");
            }
            try {
                getJdbcTemplate().afterPropertiesSet();
            } catch (IllegalArgumentException ex) {
                throw new InvalidDataAccessApiUsageException(ex.getMessage());
            }
            compileInternalExt();
            setCompiled(true);
            logCompiled();
        }
    }

    protected AbstractJdbcExtended(DataSource dataSource) {
        super(dataSource);
    }

    protected AbstractJdbcExtended(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    protected void setBatchSize(@Nullable Integer batchSize) {
        checkIfConfigurationModificationIsAllowed();
        this.batchSize = batchSize != null ? batchSize : 50;
    }

    protected void setRowMapper(@Nullable BaseRowMapper<S, T> rowMapper) {
        checkIfConfigurationModificationIsAllowed();
        this.rowMapper = rowMapper;
    }

    protected void setEntityId(@Nullable String entityId) {
        throw new NotImplementedException();
    }

    protected void setPrimaryKeyColumnName(@Nullable String primaryKeyColumnName) {
        throw new NotImplementedException();
    }

    void setParameterValuesList(PreparedStatement preparedStatement, List<?> values, @Nullable int... columnTypes)
            throws SQLException {
        ReflectionUtils.invokeMethod(this, "setParameterValues", preparedStatement, values, columnTypes);
    }

    void compileInternalExt() {
        DataSource dataSource = getJdbcTemplate().getDataSource();
        Assert.state(dataSource != null, "No DataSource set!");
        if (tableMetaDataContextExt == null) {
            this.tableMetaDataContextExt = getTableMetaDataContext();
            Assert.state(tableMetaDataContextExt != null, "No table data context set!");
            this.tableMetaDataContextExt.processMetaData(dataSource, getColumnNames(), getGeneratedKeyNames());
            this.sqlStringExt = tableMetaDataContextExt.createInsertString(getGeneratedKeyNames());
            this.intColumnTypes = tableMetaDataContextExt.createInsertTypes();
            this.tableColumnsExt = tableMetaDataContextExt.getTableColumns();
            onCompileInternal();
        }
    }

    void checkCompiledExt() {
        if (!isCompiled()) {
            logger.debug("Not compiled JDBC provider before Extended execution - invoking compile");
            compileExt();
        }
    }

    private void setCompiled(Boolean value) {
        ReflectionUtils.setFieldValue(this, "compiled", value, true);
    }

    TableMetaDataContext getTableMetaDataContext() {
        return ReflectionUtils.getFieldValue(this, "tableMetaDataContext", true);
    }

    protected void logCompiled() {
        if (logger.isDebugEnabled()) {
            logger.debug("Compiled for table [" + getTableName() + "]");
        }
    }
}
