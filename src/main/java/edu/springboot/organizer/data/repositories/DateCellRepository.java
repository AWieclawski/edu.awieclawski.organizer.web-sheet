package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.data.repositories.base.BaseSequenceService;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.mappers.DateCellRowMapper;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository(DateCellRepository.BEAN_NAME)
@DependsOn(BaseSequenceService.BEAN_NAME)
public class DateCellRepository extends BaseRepository<DateCell, DateCellDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.DateCellRepository";

    public DateCellRepository(JdbcTemplate jdbcTemplate,
                              NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                              BaseSequenceService<DateCell, DateCellDto> dateCellRetryHandler
    ) {
        super(jdbcTemplate, namedParameterJdbcTemplate, dateCellRetryHandler);
    }

    @Override
    public DateCell findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<DateCell> findAll() {
        return super.findAll();
    }

    public List<DateCell> findDateCellsByIds(List<String> ids) {
        return findEntitiesByIds(ids);
    }

    @Override
    public Long howMany() {
        return super.howMany();
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
