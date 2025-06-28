package edu.springboot.organizer.data.repositories.base;

import edu.springboot.organizer.data.exceptions.NoEntitySavedException;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.dto.RetryDataDto;
import edu.springboot.organizer.generator.dtos.base.BaseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

@Slf4j
public abstract class BaseRepository<S extends BaseEntity, T extends BaseDto> extends BaseDao<S, T> {

    private final BaseSequenceService<S, T> idGenerator;

    protected BaseRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, BaseSequenceService<S, T> idGenerator) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
        this.idGenerator = idGenerator;
    }

    /**
     * Entity Id must be assigned
     *
     * @param entityParameters
     * @param entity
     * @return
     * @throws InstantiationException
     */
    protected S insertEntity(Map<String, Object> entityParameters, S entity) throws InstantiationException {
        insertEntityExecute(entityParameters);
        return entity;
    }

    private void insertEntityExecute(Map<String, Object> entityParameters) throws InstantiationException {
        int result = executeEntityInsert(entityParameters);
        if (result == 0) {
            throw new NoEntitySavedException("No Entity saved");
        }
    }

    protected RetryDataDto<S, T> getRetryDataDto(S entity) {
        return RetryDataDto.<S, T>builder()
                .entity(entity)
                .rowMapper(getRowMapper())
                .baseIdKey(BaseEntity.BaseConst.ID.getColumn())
                .insertMethod((prs, ent) -> {
                    try {
                        return insertEntity(prs, ent);
                    } catch (InstantiationException e) { // should pass all persistence exceptions
                        log.warn("InstantiationException error {}", e.getMessage());
                    }
                    return null;
                })
                .build();
    }

    protected BaseSequenceService<S, T> getBaseIdGenerator() {
        return this.idGenerator;
    }

}
