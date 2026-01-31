package edu.springboot.organizer.data.daos.base.jdbc;

import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class ExtendedUpdateJdbcProvider<S extends BaseEntity, T extends BaseDto> extends AbstractJdbcUpdateExtended<S,T> implements ExtendedJdbcOperations<S,T> {

    public ExtendedUpdateJdbcProvider(DataSource dataSource) {
        super(dataSource);
    }

    public ExtendedUpdateJdbcProvider(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public int executeExt(Map<String, Object> args) {
        return doExecuteExt(args);
    }

    @Override
    public int[][] executeBatchExt(List<T> dtos) {
        return doExecuteBatchExt(dtos);
    }

    @Override
    public ExtendedUpdateJdbcProvider<S, T> withBatchSize(Integer batchSize) {
        setBatchSize(batchSize);
        return this;
    }

    @Override
    public ExtendedUpdateJdbcProvider<S, T> withRawMapper(BaseRowMapper<S, T> rowMapper) {
        setRowMapper(rowMapper);
        return this;
    }

    @Override
    public ExtendedUpdateJdbcProvider<S, T> withEntityId(String entityId) {
        setEntityId(entityId);
        return this;
    }

    @Override
    public ExtendedUpdateJdbcProvider<S, T> withIdColumnName(String idColumnName) {
        setPrimaryKeyColumnName(idColumnName);
        return this;
    }

    @Override
    public ExtendedUpdateJdbcProvider<S, T> withTableNameExt(String tableName) {
        setTableName(tableName);
        return this;
    }


}
