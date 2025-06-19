package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.generator.dtos.DateCellDto;
import edu.springboot.organizer.generator.mappers.DateCellMapper;
import edu.springboot.organizer.generator.mappers.DateCellRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository(DateCellRepository.BEAN_NAME)
public class DateCellRepository extends BaseRepository<DateCell, DateCellDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.VisitorRepository";

    public DateCellRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
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

    @Transactional
    @Override
    public DateCellDto persistEntity(DateCell visitor) {
        DateCell created = createDateCell(visitor);
        if (created != null) {
            return DateCellMapper.toDto(visitor);
        }
        return null;
    }

    public void deleteAll() {
        String query = String.format("DELETE FROM %s;", getTableName());
        jdbcExecuteSafe(query);
    }


    public void modifyDataBase(String sql) {
        jdbcExecuteUnsafe(sql);
    }

    @Transactional(readOnly = true)
    public Long howMany() {
        String query = String.format("SELECT COUNT(*) FROM %s;", getTableName());
        return jdbcQueryForObjectQuantity(query);
    }

    @Transactional
    public DateCell createDateCell(DateCell visitor) {
        Map<String, Object> parameters = DateCellMapper.toMap(visitor);
        return insertEntity(parameters, visitor);
    }


    @Override
    public RowMapper<DateCellDto> getRowMapper() {
        return new DateCellRowMapper();
    }

    @Override
    protected String getTableName() {
        return DateCell.TABLE_NAME;
    }

}
