package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.data.repositories.base.BaseSequenceService;
import edu.springboot.organizer.rest.dtos.VisitorDto;
import edu.springboot.organizer.rest.mappers.VisitorRowMapper;
import edu.springboot.organizer.rest.mappers.base.BaseRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository(VisitorRepository.BEAN_NAME)
@DependsOn(BaseSequenceService.BEAN_NAME)
public class VisitorRepository extends BaseRepository<Visitor, VisitorDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.VisitorRepository";

    public VisitorRepository(JdbcTemplate jdbcTemplate,
                             NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                             BaseSequenceService<Visitor, VisitorDto> visitorRetryHandler) {
        super(jdbcTemplate, namedParameterJdbcTemplate, visitorRetryHandler);
    }

    public List<Visitor> findVisitorsByTimestampIsBetween(String startDate, String endDate) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("start", startDate)
                .addValue("end", endDate);
        String query = String.format("SELECT * FROM %s WHERE %s >= DATETIME(:start) AND %s <= DATETIME(:end) ",
                getTableName(), Visitor.Const.TIMESTAMP.getColumn(), Visitor.Const.TIMESTAMP.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters);
    }

    @Override
    public Visitor findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<Visitor> findAll() {
        return super.findAll();
    }

    @Override
    public Long howMany() {
        return super.howMany();
    }

    @Override
    public BaseRowMapper<Visitor, VisitorDto> getBaseRowMapper() {
        return new VisitorRowMapper();
    }

    @Override
    public String getTableName() {
        return Visitor.TABLE_NAME;
    }

    @Override
    public String getSqlTableCreator() {
        return Visitor.getSqlTableCreator();
    }
}
