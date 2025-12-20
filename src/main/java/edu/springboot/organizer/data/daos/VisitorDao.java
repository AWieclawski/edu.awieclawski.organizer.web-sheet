package edu.springboot.organizer.data.daos;

import edu.springboot.organizer.data.daos.base.BaseEntityDao;
import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.data.daos.base.BaseSequenceService;
import edu.springboot.organizer.web.dtos.VisitorDto;
import edu.springboot.organizer.web.mappers.VisitorRowMapper;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(VisitorDao.BEAN_NAME)
@DependsOn(BaseSequenceService.BEAN_NAME)
public class VisitorDao extends BaseEntityDao<Visitor, VisitorDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.daos.VisitorDao";

    public VisitorDao(BaseSequenceService<Visitor, VisitorDto> idGenerator,
                      JdbcTemplate jdbcTemplate,
                      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(idGenerator, jdbcTemplate, namedParameterJdbcTemplate);
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

    public List<Visitor> findVisitorsByTimestampIsBetween(String startDate, String endDate) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("start", startDate)
                .addValue("end", endDate);
        String query = String.format("SELECT * FROM %s WHERE %s >= DATETIME(:start) AND %s <= DATETIME(:end) ",
                getTableName(), Visitor.Const.TIMESTAMP.getColumn(), Visitor.Const.TIMESTAMP.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters);
    }

    public List<Visitor> findVisitorsByIP(String ip) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("ip", ip);
        String query = String.format("SELECT * FROM %s WHERE %s = :ip ",
                getTableName(), Visitor.Const.IP.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters);
    }

    @Override
    protected Class<VisitorDto> getClassDto() {
        return VisitorDto.class;
    }

    @Override
    protected Class<Visitor> getClassEntity() {
        return Visitor.class;
    }
}
