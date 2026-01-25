package edu.springboot.organizer.data.daos;

import edu.springboot.organizer.data.daos.base.BaseEntityDao;
import edu.springboot.organizer.data.daos.base.BaseSequenceService;
import edu.springboot.organizer.data.models.Credential;
import edu.springboot.organizer.web.dtos.CredentialDto;
import edu.springboot.organizer.web.mappers.CredentialRowMapper;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(CredentialDao.BEAN_NAME)
@DependsOn(BaseSequenceService.BEAN_NAME)
public class CredentialDao extends BaseEntityDao<Credential, CredentialDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.daos.CredentialDao";

    public CredentialDao(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            BaseSequenceService<Credential, CredentialDto> idGenerator) {
        super(idGenerator, jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public BaseRowMapper<Credential, CredentialDto> getBaseRowMapper() {
        return new CredentialRowMapper();
    }

    @Override
    protected Class<CredentialDto> getClassDto() {
        return CredentialDto.class;
    }

    @Override
    protected Class<Credential> getClassEntity() {
        return Credential.class;
    }

    @Override
    public String getTableName() {
        return Credential.TABLE_NAME;
    }

    @Override
    public String getSqlTableCreator() {
        return Credential.getSqlTableCreator();
    }

    public Optional<Credential> findByLogin(String login) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("login", login);
        String query = String.format("SELECT * FROM %s WHERE %s = :login",
                getTableName(), Credential.Const.LOGIN.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters).stream().findFirst();
    }

    public Optional<Credential> findByEmail(String email) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("email", email);
        String query = String.format("SELECT * FROM %s WHERE %s = :email",
                getTableName(), Credential.Const.EMAIL.getColumn());
        return jdbcNamedParametersQuery(query, namedParameters).stream().findFirst();
    }
}
