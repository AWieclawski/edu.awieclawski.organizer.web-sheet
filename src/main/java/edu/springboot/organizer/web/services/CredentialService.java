package edu.springboot.organizer.web.services;

import edu.springboot.organizer.data.models.Credential;
import edu.springboot.organizer.data.repositories.CredentialRepository;
import edu.springboot.organizer.web.dtos.CredentialDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import edu.springboot.organizer.web.services.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service(value = CredentialService.BEAN_NAME)
@DependsOn(CredentialRepository.BEAN_NAME)
public class CredentialService extends BaseService<Credential, CredentialDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.web.services.CredentialService";

    private final CredentialRepository credentialRepository;

    public CredentialService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
        setEntityClass();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CredentialDto createCredential(Credential credential) {
        return createEntity(credential);
    }

    @Transactional(readOnly = true)
    public List<CredentialDto> getAllCredentials() {
        return getAllDtos();
    }

    @Transactional(readOnly = true)
    public CredentialDto getCredentialById(String id) {
        return getEntityById(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeCredentials() {
        purgeEntities();
    }

    @Override
    public void initTable() {
        initTable(Credential.getSqlTableCreator(), Credential.TABLE_NAME);
    }

    public Optional<Credential> findByLogin(String login) {
        return credentialRepository.findByLogin(login);
    }

    public Optional<Credential> findByEmail(String email) {
        return credentialRepository.findByEmail(email);
    }

    @Override
    protected void setEntityClass() {
        this.entityClass = Credential.class;
    }

    @Override
    protected CredentialRepository getRepository() {
        return this.credentialRepository;
    }

    @Override
    protected BaseRowMapper<Credential, CredentialDto> getRowMapper() {
        return getRepository().getBaseRowMapper();
    }
}

