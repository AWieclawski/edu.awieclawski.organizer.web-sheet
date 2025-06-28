package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.BaseSequenceService;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.generator.dtos.VisitorDto;
import edu.springboot.organizer.generator.mappers.VisitorRowMapper;
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
@Repository(VisitorRepository.BEAN_NAME)
@DependsOn(BaseSequenceService.BEAN_NAME)
public class VisitorRepository extends BaseRepository<Visitor, VisitorDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.VisitorRepository";

    public VisitorRepository(JdbcTemplate jdbcTemplate,
                             NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                             BaseSequenceService<Visitor, VisitorDto> visitorRetryHandler) {
        super(jdbcTemplate, namedParameterJdbcTemplate, visitorRetryHandler);
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

    @Override
    public VisitorDto persistEntity(Visitor visitor) {
        Visitor created = getBaseIdGenerator().handleEntityInn(getRetryDataDto(visitor));
        if (created != null) {
            return getRowMapper().toDto(visitor);
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
    public BaseRowMapper<Visitor, VisitorDto> getRowMapper() {
        return new VisitorRowMapper();
    }

    @Override
    protected String getTableName() {
        return Visitor.TABLE_NAME;
    }

}
