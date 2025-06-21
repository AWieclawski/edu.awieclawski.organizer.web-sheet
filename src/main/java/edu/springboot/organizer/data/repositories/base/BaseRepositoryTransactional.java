package edu.springboot.organizer.data.repositories.base;

import edu.springboot.organizer.data.exceptions.NoEntitySavedException;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.utils.BaseDateUtils;
import edu.springboot.organizer.generator.dtos.base.BaseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

@Slf4j
public abstract class BaseRepositoryTransactional<S extends BaseEntity, T extends BaseDto> extends BaseDao<S, T> {

    protected BaseRepositoryTransactional(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    protected S insertEntity(Map<String, Object> entityParameters, S entity) throws InstantiationException {
        String timestampId = insertEntityExecute(entityParameters);
        entity.setId(timestampId);
        return entity;
    }

    private String insertEntityExecute(Map<String, Object> entityParameters) throws InstantiationException {
        String timeStampId;
        String key = S.BaseConst.ID.getColumn();
        Object objId = entityParameters.get(key);
        timeStampId = objId != null ? (String) objId : BaseDateUtils.getBaseTimestampId();
        entityParameters.put(key, timeStampId);
        int result = executeEntityInsert(entityParameters);
        if (result == 0) {
            throw new NoEntitySavedException("No Entity saved");
        }
        return timeStampId;
    }

}
