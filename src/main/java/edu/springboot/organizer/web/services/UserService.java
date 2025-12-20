package edu.springboot.organizer.web.services;

import edu.springboot.organizer.data.models.Credential;
import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.repositories.UserRepository;
import edu.springboot.organizer.utils.ReflectionUtils;
import edu.springboot.organizer.web.dtos.CredentialDto;
import edu.springboot.organizer.web.dtos.UserDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import edu.springboot.organizer.web.services.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service(value = UserService.BEAN_NAME)
@DependsOn(UserRepository.BEAN_NAME)
public class UserService extends BaseService<User, UserDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.web.services.UserService";

    private final UserRepository userRepository;

    private final CredentialService credentialService;

    public UserService(UserRepository userRepository,
                       CredentialService credentialService) {
        super();
        this.userRepository = userRepository;
        this.credentialService = credentialService;
    }

    public UserDto getCtxUser() {
        return findCtxUser();
    }

    // TODO User secure registry and service to get User form context
    private UserDto findCtxUser() {
        String userId = "TEST_USER_ID";
        return UserDto.builder()
                .created(userId)
                .name("ExampleUserName")
                .surName("ExampleUserSurname")
                .build();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserDto createUser(User user) {
        return createEntity(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return getAllDtos();
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(String id) {
        return getEntityById(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeUsers() {
        purgeEntities();
    }

    @Override
    public void initTable() {
        initTable(User.getSqlTableCreator(), User.TABLE_NAME);
    }

    @Override
    protected void setEntityClass() {
        this.entityClass = User.class;
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

