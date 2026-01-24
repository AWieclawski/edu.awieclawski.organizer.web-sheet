package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.daos.UserDao;
import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.web.dtos.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository(UserRepository.BEAN_NAME)
@DependsOn(UserDao.BEAN_NAME)
public class UserRepository extends BaseRepository<User, UserDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.UserRepository";

    public UserRepository(UserDao userDao) {
        super(userDao);
    }

    @Override
    public User findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<User> findAll() {
        return super.findAll();
    }

    @Override
    public Long howMany() {
        return super.howMany();
    }

    public Optional<User> findByCredential(String credentialId) {
        return ((UserDao) getBaseDao()).findByCredential(credentialId);
    }

}

