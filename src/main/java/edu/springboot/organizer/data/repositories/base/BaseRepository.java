package edu.springboot.organizer.data.repositories.base;

import edu.springboot.organizer.data.exceptions.NoEntitySavedException;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.dto.RetryDataDto;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public abstract class BaseRepository<S extends BaseEntity, T extends BaseDto> extends BaseDao<S, T> {

    private final BaseSequenceService<S, T> idGenerator;

    protected BaseRepository(JdbcTemplate jdbcTemplate,
                             NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                             BaseSequenceService<S, T> idGenerator) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
        this.idGenerator = idGenerator;
    }

    public abstract String getSqlTableCreator();

    @Override
    public S persistEntity(S entity) {
        return getBaseIdGenerator().handleEntityInn(getRetryDataDto(entity));
    }

    public void modifyDataBase(String sql) {
        jdbcExecuteUnsecured(sql);
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

    public S updateEntity(Map<String, Object> entityParameters, S entity) throws InstantiationException {
        int result = executeEntityUpdate(entityParameters);
        if (result == 0) {
            throw new NoEntitySavedException("No Entity updated");
        }
        return findById(entity.getId());
    }

    public S updateDto(Map<String, Object> entityParameters, T dto) throws InstantiationException {
        int result = executeEntityUpdate(entityParameters);
        if (result == 0) {
            throw new NoEntitySavedException("No Entity updated");
        }
        return findById(dto.getCreated());
    }

    protected RetryDataDto<S, T> getRetryDataDto(S entity) {
        return RetryDataDto.<S, T>builder()
                .entity(entity)
                .rowMapper(getBaseRowMapper())
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

    public S findById(String id) {
        String query = String.format("SELECT * FROM %s  WHERE %s = ?;",
                getTableName(), BaseEntity.BaseConst.ID.getColumn());
        return jdbcQueryForObject(query, id);
    }

    public List<S> findAll() {
        String query = String.format("SELECT * FROM %s;", getTableName());
        return jdbcQuery(query);
    }

    public void deleteAll() {
        String query = String.format("DELETE FROM %s;", getTableName());
        jdbcExecuteSafe(query);
    }

    public Integer deleteById(String id) {
        String query = String.format("DELETE FROM %s WHERE %s = ?;", getTableName(), BaseEntity.BaseConst.ID.getColumn());
        return jdbcUpdate(query, id);
    }

    public void deleteAllById(Set<String> ids) {
        String expression = String.join("', '", ids);
        String query = String.format("DELETE FROM %s WHERE %s IN ('%s');", getTableName(), BaseEntity.BaseConst.ID.getColumn(), expression);
        jdbcExecuteSafe(query);
    }

    protected Long howMany() {
        String query = String.format("SELECT COUNT(*) FROM %s;", getTableName());
        return jdbcQueryForObjectQuantity(query);
    }

    private void insertEntityExecute(Map<String, Object> entityParameters) throws InstantiationException {
        int result = executeEntityInsert(entityParameters);
        if (result == 0) {
            throw new NoEntitySavedException("No Entity saved");
        }
    }

}
