package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.BaseSequenceService;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.generator.dtos.DateCellDto;
import edu.springboot.organizer.generator.mappers.DateCellRowMapper;
import edu.springboot.organizer.generator.mappers.base.BaseRowMapper;
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

    public List<DateCellDto> findDateCellsByTimestamp(String dateTime) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("date", dateTime);
        String query = String.format("SELECT * FROM %s WHERE %s = DATETIME(:date)",
                getTableName(), DateCell.Const.DATE.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters);
    }


    public List<DateCellDto> findDateCellsByMonthRecordId(String monthRecordId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("monthId", monthRecordId);
        String query = String.format("SELECT * FROM %s WHERE %s = :monthId",
                getTableName(), DateCell.Const.MONTH_RECORD.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters);
    }

    public DateCellDto findById(String id) {
        String query = String.format("SELECT * FROM %s  WHERE %s = ?;",
                getTableName(), BaseEntity.BaseConst.ID.getColumn());
        return jdbcQueryForObject(query, id);
    }

    public List<DateCellDto> findAll() {
        String query = String.format("SELECT * FROM %s;", getTableName());
        return jdbcQuery(query);
    }

    @Override
    public DateCellDto persistEntity(DateCell dateCell) {
        DateCell created = getBaseIdGenerator().handleEntityInn(getRetryDataDto(dateCell));
        if (created != null) {
            return getRowMapper().toDto(dateCell);
        }
        return null;
    }

    public void deleteAll() {
        String query = String.format("DELETE FROM %s;", getTableName());
        jdbcExecuteSafe(query);
    }


    public void modifyDataBase(String sql) {
        jdbcExecuteUnsecured(sql);
    }

    public Long howMany() {
        String query = String.format("SELECT COUNT(*) FROM %s;", getTableName());
        return jdbcQueryForObjectQuantity(query);
    }

    @Override
    public BaseRowMapper<DateCell, DateCellDto> getRowMapper() {
        return new DateCellRowMapper();
    }

    @Override
    protected String getTableName() {
        return DateCell.TABLE_NAME;
    }

}
