package edu.springboot.organizer.data.daos.base.jdbc;

import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;

import java.util.List;
import java.util.Map;

public interface ExtendedJdbcOperations<S extends BaseEntity, T extends BaseDto>  {

    int[][] executeBatchExt(List<T> dtos);

    int executeExt(Map<String, Object> args);

    ExtendedJdbcOperations<S, T> withBatchSize(Integer batchSize);

    ExtendedJdbcOperations<S, T> withRawMapper(BaseRowMapper<S, T> rowMapper);

    ExtendedJdbcOperations<S, T> withEntityId(String entityId);

    ExtendedJdbcOperations<S, T> withIdColumnName(String idColumnName);

    ExtendedJdbcOperations<S, T> withTableNameExt(String tableName);
}
