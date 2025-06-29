package edu.springboot.organizer.generator.services;

import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.repositories.UserRepository;
import edu.springboot.organizer.generator.dtos.UserDto;
import edu.springboot.organizer.generator.exceptions.ResultNotFoundException;
import edu.springboot.organizer.generator.services.base.BaseService;
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

    public static final String BEAN_NAME = "edu.springboot.organizer.generator.services.UserService";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserDto createUser(User user) {
        return insertEntity(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            log.error("Users not found! {}", e.getMessage());
        }
        throw new ResultNotFoundException("all users found failed!");
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(String id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            log.error("User [{}] not found! {}", id, e.getMessage());
        }
        throw new ResultNotFoundException("all users found failed!");
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeUsers() {
        try {
            userRepository.deleteAll();
        } catch (Exception e) {
            log.error("Purge failed! {}", e.getMessage());
        }
    }

    public void initTable() {
        String sql = User.getSqlTableCreator();
        log.warn("Crating table [{}]", User.TABLE_NAME);
        try {
            userRepository.modifyDataBase(sql);
        } catch (Exception e) {
            log.error("Modify failed! {}", e.getMessage());
        }
    }

    @Override
    protected UserRepository getRepository() {
        return this.userRepository;
    }
}

