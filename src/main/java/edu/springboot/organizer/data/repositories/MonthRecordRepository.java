package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.MonthRecord;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.data.repositories.base.BaseSequenceService;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.mappers.MonthRecordRowMapper;
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
@Repository(MonthRecordRepository.BEAN_NAME)
@DependsOn(BaseSequenceService.BEAN_NAME)
public class MonthRecordRepository extends BaseRepository<MonthRecord, MonthRecordDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.MonthRecordRepository";

    public MonthRecordRepository(JdbcTemplate jdbcTemplate,
                                 NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                 BaseSequenceService<MonthRecord, MonthRecordDto> monthRecordRetryHandler
    ) {
        super(jdbcTemplate, namedParameterJdbcTemplate, monthRecordRetryHandler);
    }

    @Override
    public MonthRecord findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<MonthRecord> findAll() {
        return super.findAll();
    }

    @Override
    public Long howMany() {
        return super.howMany();
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
