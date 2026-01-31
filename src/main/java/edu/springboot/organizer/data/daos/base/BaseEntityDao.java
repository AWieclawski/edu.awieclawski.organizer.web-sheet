package edu.springboot.organizer.data.daos.base;

import edu.springboot.organizer.data.daos.base.dtos.RetryDataDto;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Slf4j
public abstract class BaseEntityDao<S extends BaseEntity, T extends BaseDto> extends BaseDao<S, T> {

    private final BaseSequenceService<S, T> idGenerator;

    public BaseEntityDao(BaseSequenceService<S, T> idGenerator,
                         JdbcTemplate jdbcTemplate,
                         NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
        this.idGenerator = idGenerator;
    }

    public S persistEntity(S entity) {
        return getBaseIdGenerator().handleEntityInn(getRetryDataDto(entity));
    }

    protected BaseSequenceService<S, T> getBaseIdGenerator() {
        return this.idGenerator;
    }

    protected RetryDataDto<S, T> getRetryDataDto(S entity) {
        return RetryDataDto.<S, T>builder()
                .entity(entity)
                .baseIdKey(BaseEntity.BaseConst.ID.getColumn())
                .insertMethod((ent) -> {
                    try {
                        return insertEntity(ent);
                    } catch (InstantiationException e) { // should pass all persistence exceptions
                        log.warn("InstantiationException error {}", e.getMessage());
                    }
                    return null;
                })
                .build();
    }

}
