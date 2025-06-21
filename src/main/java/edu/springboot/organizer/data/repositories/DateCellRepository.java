package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.BaseRepositoryTransactional;
import edu.springboot.organizer.data.repositories.handlers.TransactionHandler;
import edu.springboot.organizer.generator.dtos.DateCellDto;
import edu.springboot.organizer.generator.mappers.DateCellMapper;
import edu.springboot.organizer.generator.mappers.DateCellRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository(DateCellRepository.BEAN_NAME)
public class DateCellRepository extends BaseRepositoryTransactional<DateCell, DateCellDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.DateCellRepository";

    private final TransactionHandler transactionHandler;

    public DateCellRepository(JdbcTemplate jdbcTemplate,
                              NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                              TransactionHandler transactionHandler) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
        this.transactionHandler = transactionHandler;
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
    public DateCellDto persistEntity(DateCell dateCell) {
        DateCell created = transactionHandler.runInNewTransactionFunction(this::createDateCell, dateCell);
        if (created != null) {
            return DateCellMapper.toDto(dateCell);
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

    private DateCell createDateCell(DateCell dateCell) {
        Map<String, Object> parameters = DateCellMapper.toMap(dateCell);
        try {
            return insertEntity(parameters, dateCell);
        } catch (InstantiationException i) {
            log.warn("Inserting DateCele failed {} | {}", parameters, i.getMessage());
        }
        return null;
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
