package edu.springboot.organizer.data.daos.base;

import edu.springboot.organizer.data.daos.base.jdbc.ExtendedJdbcProvider;
import edu.springboot.organizer.data.daos.base.jdbc.ExtendedUpdateJdbcProvider;
import edu.springboot.organizer.data.exceptions.NoEntitySavedException;
import edu.springboot.organizer.data.exceptions.SanitizeQueryException;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.springboot.organizer.utils.BaseDateUtils.getBaseTimestampId;
import static edu.springboot.organizer.utils.BaseStringUtils.replaceLastDigitsIncreasedByThree;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseDao<S extends BaseEntity, T extends BaseDto> {

    private static final Set<String> FORBIDDEN_EXP = Collections
            .unmodifiableSet((Set<String>) Stream.of("DROPDATABASE", "DROPTABLE")
                    .collect(Collectors.toCollection(HashSet::new)));

    private final JdbcTemplate jdbcTemplate;

    /**
     * https://www.baeldung.com/spring-jdbctemplate-in-list
     */
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void modifyDataBase(String sql) {
        jdbcExecuteUnsecured(sql);
    }


    public List<T> insertDtos(List<T> dtos) throws InstantiationException {
        return executeInsertDtos(dtos);
    }

    public List<T> updateDtos(List<T> dtos) throws InstantiationException {
        return executeUpdateDtos(dtos);
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

    public S findById(String id) {
        String query = String.format("SELECT * FROM %s  WHERE %s = ?;",
                getTableName(), BaseEntity.BaseConst.ID.getColumn());
        return jdbcQueryForObject(query, id);
    }

    public List<S> findEntitiesByIds(List<String> ids) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("ids", new HashSet<>(ids));
        String query = String.format("SELECT * FROM %s WHERE %s IN (:ids) ",
                getTableName(), BaseEntity.BaseConst.ID.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters);
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

    public int deleteByIdSet(Set<String> ids) {
        String query = String.format("DELETE FROM %s WHERE %s IN (:ids);", getTableName(), BaseEntity.BaseConst.ID.getColumn());
        Map<String, ?> namedParameters = Collections.singletonMap("ids", ids);
        return jdbcNamedParamsUpdate(query, namedParameters);
    }

    public Long howMany() {
        String query = String.format("SELECT COUNT(*) FROM %s;", getTableName());
        return jdbcQueryForObjectQuantity(query);
    }

    /**
     * Entity Id must be assigned
     *
     * @param entity
     * @return
     * @throws InstantiationException
     */
    protected S insertEntity(S entity) throws InstantiationException {
        Map<String, Object> entityParameters = getBaseRowMapper().toMap(entity);
        insertEntityExecute(entityParameters);
        return entity;
    }

    protected void jdbcExecuteUnsecured(String query) {
        jdbcExecute(query, false);
    }

    protected void jdbcExecuteSafe(String query) {
        jdbcExecute(query, true);
    }

    protected List<S> jdbcQuery(String query) {
        sanitizeQuery(query);
        return getJdbcTemplate().query(query, getBaseRowMapper());
    }

    protected S jdbcQueryForObject(String query, String id) {
        sanitizeQuery(query);
        return getJdbcTemplate().queryForObject(query, getBaseRowMapper(), id);
    }

    protected int jdbcUpdate(String sql, String id) {
        sanitizeQuery(sql);
        return getJdbcTemplate().update(sql, id);
    }

    /**
     * https://gist.github.com/chrisblakely01/504cb234e49a57d6c97932efffcd93b2
     *
     * @param sql
     * @param params
     * @return
     */
    protected int jdbcNamedParamsUpdate(String sql, Map<String, ?> params) {
        sanitizeQuery(sql);
        return getNamedParameterJdbcTemplate().update(sql, params);
    }

    protected Long jdbcQueryForObjectQuantity(String query) {
        sanitizeQuery(query);
        return getJdbcTemplate().queryForObject(query, Long.class);
    }

    protected List<S> jdbcNamedParametersQuery(String query, SqlParameterSource namedParameters) {
        sanitizeQuery(query);
        return getNamedParameterJdbcTemplate()
                .query(query,
                        namedParameters,
                        (rs, rowNum) -> getBaseRowMapper().mapRow(rs, rowNum));
    }

    List<T> executeUpdateDtos(List<T> dtos) throws InstantiationException {
        ExtendedUpdateJdbcProvider<S, T> extendedJdbcProvider = getExtendedUpdateJdbcProvider()
                .withTableNameExt(getTableName())
                .withIdColumnName(BaseEntity.BaseConst.ID.getColumn())
                .withBatchSize(50)
                .withRawMapper(getBaseRowMapper());
        int[][] result = extendedJdbcProvider.executeBatchExt(dtos);
        if (result != null && result.length > 0) {
            return dtos;
        }
        return new ArrayList<>();
    }

    List<T> executeInsertDtos(List<T> dtos) throws InstantiationException {
        String[] baseTimestampArr = new String[1];
        baseTimestampArr[0] = getBaseTimestampId();
        dtos.forEach(it -> {
            if (it.getCreated() != null) {
                throw new RuntimeException("Inserted dtos must have null ID [" + it.getCreated() + "] " + getTableName());
            }
            it.setCreated(baseTimestampArr[0]);
            baseTimestampArr[0] = replaceLastDigitsIncreasedByThree(baseTimestampArr[0]);
        });
        ExtendedJdbcProvider<S, T> extendedJdbcProvider = getExtendedJdbcProvider()
                .withTableName(getTableName())
                .withBatchSize(50)
                .withRawMapper(getBaseRowMapper());
        int[][] result = extendedJdbcProvider.executeBatchExt(dtos);
        if (result != null && result.length > 0) {
            return dtos;
        }
        return new ArrayList<>();
    }

    private void jdbcExecute(String query, boolean safe) {
        if (safe) {
            sanitizeQuery(query);
        } else {
            log.warn("Unsafe execute query [{}]", query);
        }
        getJdbcTemplate().execute(query);
    }

    private JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    private NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return this.namedParameterJdbcTemplate;
    }

    private DataSource getDataSource() {
        return getJdbcTemplate().getDataSource();
    }

    private ExtendedJdbcProvider<S, T> getExtendedJdbcProvider() throws InstantiationException {
        DataSource dataSource = getDataSource();
        if (dataSource != null) {
            return new ExtendedJdbcProvider<>(dataSource);
        }
        throw new InstantiationException("No DataSource instantiation!");
    }

    private ExtendedUpdateJdbcProvider<S, T> getExtendedUpdateJdbcProvider() throws InstantiationException {
        DataSource dataSource = getDataSource();
        if (dataSource != null) {
            return new ExtendedUpdateJdbcProvider<>(dataSource);
        }
        throw new InstantiationException("No DataSource instantiation!");
    }

    private void sanitizeQuery(final String sql) {
        String tmp = sql.replaceAll("\\s+", "");
        AtomicBoolean test = new AtomicBoolean(false);
        FORBIDDEN_EXP.forEach(it -> {
            if (tmp.contains(it)) {
                test.set(true);
            }
        });
        if (test.get()) {
            throw new SanitizeQueryException("Forbidden expression in sql");
        }
    }

    private void insertEntityExecute(Map<String, Object> entityParameters) throws InstantiationException {
        int result = executeEntityInsert(entityParameters);
        if (result == 0) {
            throw new NoEntitySavedException("No Entity saved");
        }
    }

    private int executeEntityInsert(Map<String, Object> entityParameters) throws InstantiationException {
        return getExtendedJdbcProvider().withTableName(getTableName()).execute(entityParameters);
    }

    private int executeEntityUpdate(Map<String, Object> entityParameters) throws InstantiationException {
        Object idObjectValue = entityParameters.get(BaseEntity.BaseConst.ID.getColumn());
        if (idObjectValue != null) {
            String entityId = (String) idObjectValue;

            ExtendedUpdateJdbcProvider<S, T> extendedJdbcProvider = getExtendedUpdateJdbcProvider()
                    .withTableNameExt(getTableName())
                    .withIdColumnName(BaseEntity.BaseConst.ID.getColumn())
                    .withEntityId(entityId);
            entityParameters.remove(BaseEntity.BaseConst.ID.getColumn());
            return extendedJdbcProvider.executeExt(entityParameters);

        }
        log.warn("No Entity Id to update [{}]", getTableName());
        return 0;
    }


    public abstract String getSqlTableCreator();

    public abstract String getTableName();

    public abstract BaseRowMapper<S, T> getBaseRowMapper();

    protected abstract Class<T> getClassDto();

    protected abstract Class<S> getClassEntity();
}
