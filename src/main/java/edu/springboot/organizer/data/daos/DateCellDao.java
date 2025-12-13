package edu.springboot.organizer.data.daos;

import edu.springboot.organizer.data.daos.base.BaseEntityDao;
import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.daos.base.BaseSequenceService;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.mappers.DateCellRowMapper;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(DateCellDao.BEAN_NAME)
@DependsOn(BaseSequenceService.BEAN_NAME)
public class DateCellDao extends BaseEntityDao<DateCell, DateCellDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.daos.DateCellDao";

    public DateCellDao(JdbcTemplate jdbcTemplate,
                       NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                       BaseSequenceService<DateCell, DateCellDto> idGenerator) {
        super(idGenerator, jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public BaseRowMapper<DateCell, DateCellDto> getBaseRowMapper() {
        return new DateCellRowMapper();
    }

    @Override
    protected Class<DateCellDto> getClassDto() {
        return DateCellDto.class;
    }

    @Override
    protected Class<DateCell> getClassEntity() {
        return DateCell.class;
    }

    @Override
    public String getTableName() {
        return DateCell.TABLE_NAME;
    }

    @Override
    public String getSqlTableCreator() {
        return DateCell.getSqlTableCreator();
    }

    public List<DateCell> findDateCellsByDate(String dateTime) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("date", dateTime);
        String query = String.format("SELECT * FROM %s WHERE DATE(%s) = DATE(:date)",
                getTableName(), DateCell.Const.DATE.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters);
    }

    public List<DateCell> findDateCellsByMonthRecordId(String monthRecordId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("monthId", monthRecordId);
        String query = String.format("SELECT * FROM %s WHERE %s = :monthId",
                getTableName(), DateCell.Const.MONTH_RECORD.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters);
    }

    public List<DateCell> findDateCellsByDateAndMonthRecordId(String dateTime, String monthRecordId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("date", dateTime)
                .addValue("monthId", monthRecordId);
        String query = String.format("SELECT * FROM %s WHERE DATE(%s) = DATE(:date) AND %s = :monthId",
                getTableName(), DateCell.Const.DATE.getColumn(), DateCell.Const.MONTH_RECORD.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters);
    }

}
