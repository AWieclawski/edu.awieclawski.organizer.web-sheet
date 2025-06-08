package edu.awieclawski.organizer.data.repositories;

import edu.awieclawski.organizer.data.models.Visitor;
import edu.awieclawski.organizer.generator.dtos.VisitorDto;
import edu.awieclawski.organizer.generator.mappers.VisitorMapper;
import edu.awieclawski.organizer.generator.mappers.VisitorRowMapper;
import edu.awieclawski.organizer.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.awieclawski.organizer.data.models.Visitor.TABLE_NAME;

@Repository(VisitorRepository.BEAN_NAME)
@RequiredArgsConstructor
public class VisitorRepository {

    public static final String BEAN_NAME = "edu.awieclawski.organizer.data.repositories.VisitorRepository";

    private final JdbcTemplate jdbcTemplate;

    /**
     * https://www.baeldung.com/spring-jdbctemplate-in-list
     */
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<VisitorDto> findVisitorsByTimestampIsBetween(String startDate, String endDate) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("start", startDate)
                .addValue("end", endDate);
        String query = String.format("SELECT * FROM %s WHERE %s >= DATETIME(:start) AND timestamp <= DATETIME(:end) ",
                TABLE_NAME, Visitor.Const.TIMESTAMP.getColumn());
        return namedParameterJdbcTemplate.query(query, namedParameters,
                (rs, rowNum) -> getRowMapper().mapRow(rs, rowNum)
        );
    }

    public VisitorDto findById(String id) {
        String query = String.format("SELECT * FROM %s  WHERE %s = ?;",
                TABLE_NAME, Visitor.BaseConst.ID.getColumn());
        return jdbcTemplate.queryForObject(query, getRowMapper(), id);
    }

    public List<VisitorDto> findAll() {
        String query = String.format("SELECT * FROM %s;", TABLE_NAME);
        return jdbcTemplate.query(query, getRowMapper());
    }

    public VisitorDto save(Visitor visitor) {
        int result = addVisitor(visitor);
        return result > 0 ? VisitorMapper.toDto(visitor) : null;
    }

    public void deleteAll() {
        String query = String.format("DELETE FROM %s;", TABLE_NAME);
        jdbcTemplate.execute(query);
    }


    public void modifyDataBase(String sql) {
        jdbcTemplate.execute(sql);
    }

    public Long howMany() {
        return jdbcTemplate.queryForObject(
                String.format("SELECT COUNT(*) FROM %s;", TABLE_NAME), Long.class);
    }

    public int addVisitor(Visitor visitor) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(Visitor.BaseConst.ID.getColumn(), visitor.getId());
        parameters.put(Visitor.Const.TIMESTAMP.getColumn(), DateUtils.localDateTimeToTimestamp(visitor.getTimestamp()));
        parameters.put(Visitor.Const.URL.getColumn(), visitor.getUrl());
        parameters.put(Visitor.Const.IP.getColumn(), visitor.getIp());
        parameters.put(Visitor.Const.NAME.getColumn(), visitor.getName());
        return getVisitorsInsert().execute(parameters);
    }

    public RowMapper<VisitorDto> getRowMapper() {
        return new VisitorRowMapper();
    }

    public DataSource getDataSource() {
        return jdbcTemplate.getDataSource();
    }

    public SimpleJdbcInsert getVisitorsInsert() {
        return new SimpleJdbcInsert(getDataSource()).withTableName(TABLE_NAME);
    }

}
