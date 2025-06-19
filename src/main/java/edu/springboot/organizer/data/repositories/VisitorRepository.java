package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.generator.dtos.VisitorDto;
import edu.springboot.organizer.generator.mappers.VisitorMapper;
import edu.springboot.organizer.generator.mappers.VisitorRowMapper;
import edu.springboot.organizer.utils.DateUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository(VisitorRepository.BEAN_NAME)
public class VisitorRepository extends BaseRepository<Visitor, VisitorDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.VisitorRepository";

    public VisitorRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    public List<VisitorDto> findVisitorsByTimestampIsBetween(String startDate, String endDate) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("start", startDate)
                .addValue("end", endDate);
        String query = String.format("SELECT * FROM %s WHERE %s >= DATETIME(:start) AND timestamp <= DATETIME(:end) ",
                getTableName(), Visitor.Const.TIMESTAMP.getColumn());
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
    public VisitorDto insert(Visitor visitor) {
        Visitor created = createVisitor(visitor);
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

    /**
     * @param visitor
     * @return timestamp String value key
     */
    @Transactional
    public Visitor createVisitor(Visitor visitor) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), visitor.getId());
        parameters.put(Visitor.Const.TIMESTAMP.getColumn(), DateUtils.localDateTimeToTimestamp(visitor.getTimestamp()));
        parameters.put(Visitor.Const.URL.getColumn(), visitor.getUrl());
        parameters.put(Visitor.Const.IP.getColumn(), visitor.getIp());
        parameters.put(Visitor.Const.NAME.getColumn(), visitor.getName());
        return createEntity(parameters, visitor);
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
