package edu.springboot.organizer.generator.services;

import edu.springboot.organizer.data.models.Credential;
import edu.springboot.organizer.data.repositories.CredentialRepository;
import edu.springboot.organizer.generator.dtos.CredentialDto;
import edu.springboot.organizer.generator.exceptions.ResultNotFoundException;
import edu.springboot.organizer.generator.mappers.base.BaseRowMapper;
import edu.springboot.organizer.generator.services.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service(value = CredentialService.BEAN_NAME)
@DependsOn(CredentialRepository.BEAN_NAME)
public class CredentialService extends BaseService<Credential, CredentialDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.generator.services.CredentialService";

    private final CredentialRepository credentialRepository;

    public CredentialService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CredentialDto createCredential(Credential credential) {
        Credential entity = insertEntity(credential);
        log.info("Saved [{}]", entity);
        return getRowMapper().toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<CredentialDto> getAllCredentials() {
        try {
            List<Credential> entities = credentialRepository.findAll();
            return entities.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Credentials not found! {}", e.getMessage());
        }
        throw new ResultNotFoundException("all users found failed!");
    }

    @Transactional(readOnly = true)
    public CredentialDto getCredentialById(String id) {
        try {
            Credential entity = credentialRepository.findById(id);
            return getRowMapper().toDto(entity);
        } catch (Exception e) {
            log.error("Credential [{}] not found! {}", id, e.getMessage());
        }
        throw new ResultNotFoundException("Credential search by id failed!");
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeCredentials() {
        purgeEntities(getRepository().getTableName());
    }

    @Override
    public void initTable() {
        initTable(Credential.getSqlTableCreator(), Credential.TABLE_NAME);
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

