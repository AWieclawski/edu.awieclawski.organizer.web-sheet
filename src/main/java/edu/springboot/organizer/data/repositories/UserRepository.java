package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.data.repositories.base.BaseSequenceService;
import edu.springboot.organizer.web.dtos.UserDto;
import edu.springboot.organizer.web.mappers.UserRowMapper;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
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

    @Override
    public User findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<User> findAll() {
        return super.findAll();
    }

    @Override
    public Long howMany() {
        return super.howMany();
    }

    @Override
    public BaseRowMapper<User, UserDto> getBaseRowMapper() {
        return new UserRowMapper();
    }

    @Override
    public String getTableName() {
        return User.TABLE_NAME;
    }

    @Override
    public String getSqlTableCreator() {
        return User.getSqlTableCreator();
    }
}
