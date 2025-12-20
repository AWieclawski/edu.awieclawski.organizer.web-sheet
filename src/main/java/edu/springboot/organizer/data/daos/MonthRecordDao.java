package edu.springboot.organizer.data.daos;

import edu.springboot.organizer.data.daos.base.BaseEntityDao;
import edu.springboot.organizer.data.models.MonthRecord;
import edu.springboot.organizer.data.daos.base.BaseSequenceService;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.mappers.MonthRecordRowMapper;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(MonthRecordDao.BEAN_NAME)
@DependsOn(BaseSequenceService.BEAN_NAME)
public class MonthRecordDao extends BaseEntityDao<MonthRecord, MonthRecordDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.daos.MonthRecordDao";

    public MonthRecordDao(BaseSequenceService<MonthRecord, MonthRecordDto> idGenerator,
                          JdbcTemplate jdbcTemplate,
                          NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(idGenerator, jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public BaseRowMapper<MonthRecord, MonthRecordDto> getBaseRowMapper() {
        return new MonthRecordRowMapper();
    }

    @Override
    protected Class<MonthRecordDto> getClassDto() {
        return MonthRecordDto.class;
    }

    @Override
    protected Class<MonthRecord> getClassEntity() {
        return MonthRecord.class;
    }

    @Override
    public String getTableName() {
        return MonthRecord.TABLE_NAME;
    }

    @Override
    public String getSqlTableCreator() {
        return MonthRecord.getSqlTableCreator();
    }

    public List<MonthRecord> findMonthRecordBySet(String setId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("setId", setId);
        String query = String.format("SELECT * FROM %s WHERE %s = :setId",
                getTableName(), MonthRecord.Const.SET.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters);
    }

}
