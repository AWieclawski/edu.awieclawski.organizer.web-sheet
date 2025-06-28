package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.data.repositories.base.BaseSequenceService;
import edu.springboot.organizer.generator.dtos.UserDto;
import edu.springboot.organizer.generator.mappers.UserRowMapper;
import edu.springboot.organizer.generator.mappers.base.BaseRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository(UserRepository.BEAN_NAME)
@DependsOn(BaseSequenceService.BEAN_NAME)
public class UserRepository extends BaseRepository<User, UserDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.UserRepository";

    public UserRepository(JdbcTemplate jdbcTemplate,
                          NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                          BaseSequenceService<User, UserDto> userRetryHandler) {
        super(jdbcTemplate, namedParameterJdbcTemplate, userRetryHandler);
    }

    public UserDto findById(String id) {
        String query = String.format("SELECT * FROM %s  WHERE %s = ?;",
                getTableName(), BaseEntity.BaseConst.ID.getColumn());
        return jdbcQueryForObject(query, id);
    }

    public List<UserDto> findAll() {
        String query = String.format("SELECT * FROM %s;", getTableName());
        return jdbcQuery(query);
    }

    @Override
    public UserDto persistEntity(User user) {
        User created = getBaseIdGenerator().handleEntityInn(getRetryDataDto(user));
        if (created != null) {
            return getRowMapper().toDto(user);
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
    public BaseRowMapper<User, UserDto> getRowMapper() {
        return new UserRowMapper();
    }

    @Override
    protected String getTableName() {
        return User.TABLE_NAME;
    }

}
