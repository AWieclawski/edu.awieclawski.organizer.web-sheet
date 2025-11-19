package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.RecordsSet;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.data.repositories.base.BaseSequenceService;
import edu.springboot.organizer.web.dtos.RecordsSetDto;
import edu.springboot.organizer.web.mappers.RecordsSetRowMapper;
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
@Repository(RecordsSetRepository.BEAN_NAME)
@DependsOn(BaseSequenceService.BEAN_NAME)
public class RecordsSetRepository extends BaseRepository<RecordsSet, RecordsSetDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.RecordsSetRepository";

    public RecordsSetRepository(JdbcTemplate jdbcTemplate,
                                NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                BaseSequenceService<RecordsSet, RecordsSetDto> monthRecordRetryHandler
    ) {
        super(jdbcTemplate, namedParameterJdbcTemplate, monthRecordRetryHandler);
    }

    @Override
    public RecordsSet findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<RecordsSet> findAll() {
        return super.findAll();
    }

    @Override
    public Long howMany() {
        return super.howMany();
    }

    @Override
    public BaseRowMapper<RecordsSet, RecordsSetDto> getBaseRowMapper() {
        return new RecordsSetRowMapper();
    }

    @Override
    protected Class<RecordsSetDto> getClassDto() {
        return RecordsSetDto.class;
    }

    @Override
    protected Class<RecordsSet> getClassEntity() {
        return RecordsSet.class;
    }

    @Override
    public String getTableName() {
        return RecordsSet.TABLE_NAME;
    }

    @Override
    public String getSqlTableCreator() {
        return RecordsSet.getSqlTableCreator();
    }

    public List<RecordsSet> findRecordsSetByUser(String userId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("userId", userId);
        String query = String.format("SELECT * FROM %s WHERE %s = :userId",
                getTableName(), RecordsSet.Const.USER.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters);
    }

    public List<RecordsSet> findRecordsSetByMonthYearUser(int month, int year, String userId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("month", month)
                .addValue("year", year)
                .addValue("userId", userId);
        String query = String.format("SELECT * FROM %s WHERE %s = :month AND %s = :year AND %s = :userId",
                getTableName(),
                RecordsSet.Const.MONTH.getColumn(),
                RecordsSet.Const.YEAR.getColumn(),
                RecordsSet.Const.USER.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters);
    }

}
