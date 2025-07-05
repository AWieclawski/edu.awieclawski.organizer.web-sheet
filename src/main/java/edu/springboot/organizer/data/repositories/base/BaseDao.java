package edu.springboot.organizer.data.repositories.base;

import edu.springboot.organizer.data.exceptions.SanitizeQueryException;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.generator.dtos.base.BaseDto;
import edu.springboot.organizer.generator.mappers.base.BaseRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

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
public abstract class BaseDao<S extends BaseEntity, T extends BaseDto> {

    private static final Set<String> FORBIDDEN_EXP = Collections
            .unmodifiableSet((Set<String>) Stream.of("DROPDATABASE", "DROPTABLE")
                    .collect(Collectors.toCollection(HashSet::new)));

    private final JdbcTemplate jdbcTemplate;

    /**
     * https://www.baeldung.com/spring-jdbctemplate-in-list
     */
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

    private void jdbcExecute(String query, boolean safe) {
        if (safe) {
            sanitizeQuery(query);
        } else {
            log.warn("Unsafe execute query [{}]", query);
        }
        getJdbcTemplate().execute(query);
    }

    int executeEntityInsert(Map<String, Object> entityParameters) throws InstantiationException {
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
        DataSource dataSource = getDataSource();
        if (dataSource != null) {
            return new SimpleJdbcInsert(dataSource);
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

    public abstract S persistEntity(S entity);

    protected abstract String getTableName();

    protected abstract BaseRowMapper<S, T> getBaseRowMapper();
}
