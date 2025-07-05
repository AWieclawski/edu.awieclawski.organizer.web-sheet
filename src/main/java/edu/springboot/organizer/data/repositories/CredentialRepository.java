package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.Credential;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.data.repositories.base.BaseSequenceService;
import edu.springboot.organizer.generator.dtos.CredentialDto;
import edu.springboot.organizer.generator.mappers.CredentialRowMapper;
import edu.springboot.organizer.generator.mappers.base.BaseRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository(CredentialRepository.BEAN_NAME)
@DependsOn(BaseSequenceService.BEAN_NAME)
public class CredentialRepository extends BaseRepository<Credential, CredentialDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.CredentialRepository";

    public CredentialRepository(JdbcTemplate jdbcTemplate,
                                NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                BaseSequenceService<Credential, CredentialDto> credentialRetryHandler) {
        super(jdbcTemplate, namedParameterJdbcTemplate, credentialRetryHandler);
    }

    @Override
    public Credential findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<Credential> findAll() {
        return super.findAll();
    }

    @Override
    public Long howMany() {
        return super.howMany();
    }

    @Override
    public BaseRowMapper<Credential, CredentialDto> getBaseRowMapper() {
        return new CredentialRowMapper();
    }

    @Override
    public String getTableName() {
        return Credential.TABLE_NAME;
    }

    @Override
    public String getSqlTableCreator() {
        return Credential.getSqlTableCreator();
    }
}
