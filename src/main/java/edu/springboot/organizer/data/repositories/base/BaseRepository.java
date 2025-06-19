package edu.springboot.organizer.data.repositories.base;

import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.utils.BaseDateUtils;
import edu.springboot.organizer.data.utils.BaseStringUtils;
import edu.springboot.organizer.generator.dtos.base.BaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Component(BaseRepository.BEAN_NAME)
public abstract class BaseRepository<S extends BaseEntity, T extends BaseDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.base.BaseRepository";

    private static final int MAX_TRY_COUNT = 3;

    private static final Set<String> FORBIDDEN_EXP = Collections
            .unmodifiableSet((Set<String>) Stream.of("DROPDATABASE", "DROPTABLE")
                    .collect(Collectors.toCollection(HashSet::new)));

    private final JdbcTemplate jdbcTemplate;

    /**
     * https://www.baeldung.com/spring-jdbctemplate-in-list
     */
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected void jdbcExecuteUnsafe(String query) {
        jdbcExecute(query, false);
    }

    protected void jdbcExecuteSafe(String query) {
        jdbcExecute(query, true);
    }

    protected List<T> jdbcQuery(String query) {
        sanitizeQuery(query);
        return getJdbcTemplate().query(query, getRowMapper());
    }

    protected T jdbcQueryForObject(String query, String id) {
        sanitizeQuery(query);
        return getJdbcTemplate().queryForObject(query, getRowMapper(), id);
    }

    protected Long jdbcQueryForObjectQuantity(String query) {
        sanitizeQuery(query);
        return getJdbcTemplate().queryForObject(query, Long.class);
    }

    protected List<T> jdbcNamedParametersQuery(String query, SqlParameterSource namedParameters) {
        sanitizeQuery(query);
        return getNamedParameterJdbcTemplate()
                .query(query,
                        namedParameters,
                        (rs, rowNum) -> getRowMapper().mapRow(rs, rowNum));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public S insertEntity(Map<String, Object> entityParameters, S entity) {
        String timestampId = insertEntityExecute(entityParameters, 0);
        entity.setId(timestampId);
        return entity;
    }

    private void jdbcExecute(String query, Boolean safe) {
        if (safe) {
            sanitizeQuery(query);
        } else {
            log.warn("Unsafe execute query [{}]", query);
        }
        getJdbcTemplate().execute(query);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String insertEntityExecute(Map<String, Object> entityParameters, int count) {
        String timeStampId;
        String key = S.BaseConst.ID.getColumn();
        Object objId = entityParameters.get(key);
        timeStampId = objId != null ? (String) objId : BaseDateUtils.getBaseTimestampId();
        entityParameters.put(key, timeStampId);
        try {
            int result = executeEntityInsert(entityParameters);
            if (result > 0) {
                return timeStampId;
            }
        } catch (Exception e) {
            log.error("Base Id [{}] creation failed! {}", timeStampId, e.getMessage());
            if (count < MAX_TRY_COUNT) {
                BaseStringUtils.replaceLastDigitsIncreasedByOne(timeStampId);
                entityParameters.put(key, timeStampId);
                insertEntityExecute(entityParameters, ++count);
            }
        }
        return timeStampId;
    }

    private int executeEntityInsert(Map<String, Object> entityParameters) throws InstantiationException {
        return getEntityInsert().withTableName(getTableName()).execute(entityParameters);
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

    private SimpleJdbcInsert getEntityInsert() throws InstantiationException {
        if (getDataSource() != null) {
            return new SimpleJdbcInsert(getDataSource());
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
            throw new IllegalArgumentException("Forbidden expression in sql");
        }
    }

    protected abstract String getTableName();

    protected abstract RowMapper<T> getRowMapper();

    public abstract T persistEntity(S entity);
}
