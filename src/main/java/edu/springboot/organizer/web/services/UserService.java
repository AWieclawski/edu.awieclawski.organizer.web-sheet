package edu.springboot.organizer.web.services;

import edu.springboot.organizer.data.models.Credential;
import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.repositories.UserRepository;
import edu.springboot.organizer.utils.ReflectionUtils;
import edu.springboot.organizer.web.dtos.CredentialDto;
import edu.springboot.organizer.web.dtos.UserDto;
import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import edu.springboot.organizer.web.services.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service(value = UserService.BEAN_NAME)
@DependsOn(UserRepository.BEAN_NAME)
public class UserService extends BaseService<User, UserDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.web.services.UserService";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private CredentialService credentialService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserDto createUser(User user) {
        assignCredentials(user);
        User entity = insertEntity(user);
        log.info("Saved [{}]", entity);
        return getRowMapper().toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        try {
            List<User> entities = userRepository.findAll();
            return entities.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Users not found! {}", e.getMessage());
        }
        throw new ResultNotFoundException("All Users search failed!");
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(String id) {
        try {
            User entity = userRepository.findById(id);
            return getRowMapper().toDto(entity);
        } catch (Exception e) {
            log.error("User [{}] not found! {}", id, e.getMessage());
        }
        throw new ResultNotFoundException("User search by id failed!");
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeUsers() {
        purgeEntities(getRepository().getTableName());
    }

    @Override
    public void initTable() {
        initTable(User.getSqlTableCreator(), User.TABLE_NAME);
    }

    @Override
    protected UserRepository getRepository() {
        return this.userRepository;
    }

    @Override
    protected BaseRowMapper<User, UserDto> getRowMapper() {
        return getRepository().getBaseRowMapper();
    }

    private void assignCredentials(User user) {
        if (user.getCredentialId() == null) {
            CredentialDto credential = credentialService.createCredential(Credential.builder()
                    .email(UUID.randomUUID().toString())
                    .password(UUID.randomUUID().toString())
                    .login(UUID.randomUUID().toString())
                    .build());
            ReflectionUtils.setFieldValue(user, "credentialId", credential.getCreated(), true);
        }
    }
}

