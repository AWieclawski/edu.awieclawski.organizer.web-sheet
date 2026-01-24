package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.daos.CredentialDao;
import edu.springboot.organizer.data.models.Credential;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.web.dtos.CredentialDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository(CredentialRepository.BEAN_NAME)
@DependsOn(CredentialDao.BEAN_NAME)
public class CredentialRepository extends BaseRepository<Credential, CredentialDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.CredentialRepository";

    public CredentialRepository(CredentialDao credentialDao) {
        super(credentialDao);
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

    public Optional<Credential> findByLogin(String login) {
        return ((CredentialDao) getBaseDao()).findByLogin(login);
    }

}
