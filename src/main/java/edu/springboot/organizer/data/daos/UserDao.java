package edu.springboot.organizer.data.daos;

import edu.springboot.organizer.data.daos.base.BaseEntityDao;
import edu.springboot.organizer.data.daos.base.BaseSequenceService;
import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.web.dtos.UserDto;
import edu.springboot.organizer.web.mappers.UserRowMapper;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(UserDao.BEAN_NAME)
@DependsOn(BaseSequenceService.BEAN_NAME)
public class UserDao extends BaseEntityDao<User, UserDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.daos.UserDao";

    public UserDao(BaseSequenceService<User, UserDto> idGenerator,
                   JdbcTemplate jdbcTemplate,
                   NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(idGenerator, jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public BaseRowMapper<User, UserDto> getBaseRowMapper() {
        return new UserRowMapper();
    }

    public Optional<User> findByCredential(String credentialId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("credentialId", credentialId);
        String query = String.format("SELECT * FROM %s WHERE %s = :credentialId",
                getTableName(), User.Const.CREDENTIAL.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters).stream().findFirst();
    }

    @Override
    protected Class<UserDto> getClassDto() {
        return UserDto.class;
    }

    @Override
    protected Class<User> getClassEntity() {
        return User.class;
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
