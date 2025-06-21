package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.BaseRepositoryTransactional;
import edu.springboot.organizer.data.repositories.handlers.TransactionHandler;
import edu.springboot.organizer.generator.dtos.VisitorDto;
import edu.springboot.organizer.generator.mappers.VisitorMapper;
import edu.springboot.organizer.generator.mappers.VisitorRowMapper;
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
@Repository(VisitorRepository.BEAN_NAME)
public class VisitorRepository extends BaseRepositoryTransactional<Visitor, VisitorDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.VisitorRepository";

    private final TransactionHandler transactionHandler;

    public VisitorRepository(JdbcTemplate jdbcTemplate,
                             NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                             TransactionHandler transactionHandler) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
        this.transactionHandler = transactionHandler;
    }

    public List<VisitorDto> findVisitorsByTimestampIsBetween(String startDate, String endDate) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("start", startDate)
                .addValue("end", endDate);
        String query = String.format("SELECT * FROM %s WHERE %s >= DATETIME(:start) AND %s <= DATETIME(:end) ",
                getTableName(), Visitor.Const.TIMESTAMP.getColumn(), Visitor.Const.TIMESTAMP.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters);
    }

    public VisitorDto findById(String id) {
        String query = String.format("SELECT * FROM %s  WHERE %s = ?;",
                getTableName(), BaseEntity.BaseConst.ID.getColumn());
        return jdbcQueryForObject(query, id);
    }

    public List<VisitorDto> findAll() {
        String query = String.format("SELECT * FROM %s;", getTableName());
        return jdbcQuery(query);
    }

    @Transactional
    @Override
    public VisitorDto persistEntity(Visitor visitor) {
        Visitor created = transactionHandler.runInNewTransactionFunction(this::createVisitor, visitor);
        if (created != null) {
            return VisitorMapper.toDto(visitor);
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

    private Visitor createVisitor(Visitor visitor) {
        Map<String, Object> parameters = VisitorMapper.toMap(visitor);
        try {
            return insertEntity(parameters, visitor);
        } catch (InstantiationException i) {
            log.warn("Inserting DateCele failed {} | {}", parameters, i.getMessage());
        }
        return null;
    }


    @Override
    public RowMapper<VisitorDto> getRowMapper() {
        return new VisitorRowMapper();
    }

    @Override
    protected String getTableName() {
        return Visitor.TABLE_NAME;
    }

}
