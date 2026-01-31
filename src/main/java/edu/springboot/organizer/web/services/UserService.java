package edu.springboot.organizer.web.services;

import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.repositories.UserRepository;
import edu.springboot.organizer.web.dtos.UserDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import edu.springboot.organizer.web.services.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service(value = UserService.BEAN_NAME)
@DependsOn(UserRepository.BEAN_NAME)
public class UserService extends BaseService<User, UserDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.web.services.UserService";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
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

}

