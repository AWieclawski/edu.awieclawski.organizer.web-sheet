package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.generator.dtos.UserDto;
import edu.springboot.organizer.generator.mappers.UserMapper;
import edu.springboot.organizer.generator.mappers.UserRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository(UserRepository.BEAN_NAME)
public class UserRepository extends BaseRepository<User, UserDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.UserRepository";

    public UserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
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

    @Transactional
    @Override
    public UserDto persistEntity(User user) {
        User created = createUser(user);
        if (created != null) {
            return UserMapper.toDto(user);
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
    public User createUser(User user) {
        Map<String, Object> parameters = UserMapper.toMap(user);
        return insertEntity(parameters, user);
    }


    @Override
    public RowMapper<UserDto> getRowMapper() {
        return new UserRowMapper();
    }

    @Override
    protected String getTableName() {
        return User.TABLE_NAME;
    }

}
