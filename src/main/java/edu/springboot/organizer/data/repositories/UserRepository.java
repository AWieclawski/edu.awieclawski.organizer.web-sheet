package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.BaseRepositoryTransactional;
import edu.springboot.organizer.data.repositories.handlers.TransactionHandler;
import edu.springboot.organizer.generator.dtos.UserDto;
import edu.springboot.organizer.generator.mappers.UserMapper;
import edu.springboot.organizer.generator.mappers.UserRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository(UserRepository.BEAN_NAME)
public class UserRepository extends BaseRepositoryTransactional<User, UserDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.UserRepository";

    private final TransactionHandler transactionHandler;

    public UserRepository(JdbcTemplate jdbcTemplate,
                          NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                          TransactionHandler transactionHandler) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
        this.transactionHandler = transactionHandler;
    }

    public UserDto findById(String id) {
        String query = String.format("SELECT * FROM %s  WHERE %s = ?;",
                getTableName(), BaseEntity.BaseConst.ID.getColumn());
        return jdbcQueryForObject(query, id);
    }

    public List<UserDto> findAll() {
        String query = String.format("SELECT * FROM %s;", getTableName());
        return jdbcQuery(query);
    }

    @Transactional
    @Override
    public UserDto persistEntity(User user) {
        User created = transactionHandler.runInNewTransactionFunction(this::createUser, user);
        if (created != null) {
            return UserMapper.toDto(user);
        }
        return null;
    }

    public void deleteAll() {
        String query = String.format("DELETE FROM %s;", getTableName());
        jdbcExecuteSafe(query);
    }


    public void modifyDataBase(String sql) {
        jdbcExecuteUnsafe(sql);
    }

    @Transactional(readOnly = true)
    public Long howMany() {
        String query = String.format("SELECT COUNT(*) FROM %s;", getTableName());
        return jdbcQueryForObjectQuantity(query);
    }

    private User createUser(User user) {
        Map<String, Object> parameters = UserMapper.toMap(user);
        try {
            return insertEntity(parameters, user);
        } catch (InstantiationException i) {
            log.warn("Inserting DateCele failed {} | {}", parameters, i.getMessage());
        }
        return null;
    }


    @Override
    public RowMapper<UserDto> getRowMapper() {
        return new UserRowMapper();
    }

    @Override
    protected String getTableName() {
        return User.TABLE_NAME;
    }

}
