package edu.springboot.organizer.data.daos.base.jdbc;

import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExtendedJdbcProvider<S extends BaseEntity, T extends BaseDto> extends AbstractJdbcExtended<S, T>
        implements ExtendedJdbcOperations<S, T>, SimpleJdbcInsertOperations {

    public ExtendedJdbcProvider(DataSource dataSource) {
        super(dataSource);
    }

    public ExtendedJdbcProvider(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public int[][] executeBatchExt(List<T> dtos) {
        return doExecuteBatchExt(dtos);
    }

    @Override
    public int executeExt(Map<String, Object> args) {
        return doExecuteExt(args);
    }

    @Override
    public ExtendedJdbcProvider<S, T> withBatchSize(Integer batchSize) {
        setBatchSize(batchSize);
        return this;
    }

    @Override
    public ExtendedJdbcProvider<S, T> withRawMapper(BaseRowMapper<S, T> rowMapper) {
        setRowMapper(rowMapper);
        return this;
    }

    @Override
    public ExtendedJdbcProvider<S, T> withEntityId(String entityId) {
        setEntityId(entityId);
        return this;
    }

    @Override
    public ExtendedJdbcProvider<S, T> withIdColumnName(String idColumnName) {
        setPrimaryKeyColumnName(idColumnName);
        return this;
    }

    @Override
    public ExtendedJdbcProvider<S, T> withTableNameExt(String tableName) {
        setTableName(tableName);
        return this;
    }

    @Override
    public ExtendedJdbcProvider<S, T> withTableName(String tableName) {
        setTableName(tableName);
        return this;
    }

    @Override
    public ExtendedJdbcProvider<S, T> withSchemaName(String schemaName) {
        setSchemaName(schemaName);
        return this;
    }

    @Override
    public ExtendedJdbcProvider<S, T> withCatalogName(String catalogName) {
        setCatalogName(catalogName);
        return this;
    }

    @Override
    public ExtendedJdbcProvider<S, T> usingColumns(String... columnNames) {
        setColumnNames(Arrays.asList(columnNames));
        return this;
    }

    @Override
    public ExtendedJdbcProvider<S, T> usingGeneratedKeyColumns(String... columnNames) {
        setGeneratedKeyNames(columnNames);
        return this;
    }

    @Override
    public SimpleJdbcInsertOperations withoutTableColumnMetaDataAccess() {
        setAccessTableColumnMetaData(false);
        return this;
    }

    @Override
    public SimpleJdbcInsertOperations includeSynonymsForTableColumnMetaData() {
        setOverrideIncludeSynonymsDefault(true);
        return this;
    }

    @Override
    public int execute(Map<String, ?> args) {
        return doExecute(args);
    }

    @Override
    public int execute(SqlParameterSource parameterSource) {
        return doExecute(parameterSource);
    }

    @Override
    public Number executeAndReturnKey(Map<String, ?> args) {
        return doExecuteAndReturnKey(args);
    }

    @Override
    public Number executeAndReturnKey(SqlParameterSource parameterSource) {
        return doExecuteAndReturnKey(parameterSource);
    }

    @Override
    public KeyHolder executeAndReturnKeyHolder(Map<String, ?> args) {
        return doExecuteAndReturnKeyHolder(args);
    }

    @Override
    public KeyHolder executeAndReturnKeyHolder(SqlParameterSource parameterSource) {
        return doExecuteAndReturnKeyHolder(parameterSource);
    }

    @Override
    @SuppressWarnings("unchecked")
    public int[] executeBatch(Map<String, ?>... batch) {
        return doExecuteBatch(batch);
    }

    @Override
    public int[] executeBatch(SqlParameterSource... batch) {
        return doExecuteBatch(batch);
    }
}
