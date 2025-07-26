package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.models.Employee;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.data.repositories.base.BaseSequenceService;
import edu.springboot.organizer.web.dtos.EmployeeDto;
import edu.springboot.organizer.web.mappers.EmployeeRowMapper;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository(EmployeeRepository.BEAN_NAME)
@DependsOn(BaseSequenceService.BEAN_NAME)
public class EmployeeRepository extends BaseRepository<Employee, EmployeeDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.EmployeeRepository";

    @Override
    public Employee findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<Employee> findAll() {
        return super.findAll();
    }

    @Override
    public Long howMany() {
        return super.howMany();
    }

    @Override
    public BaseRowMapper<Employee, EmployeeDto> getBaseRowMapper() {
        return new EmployeeRowMapper();
    }

    @Override
    public String getTableName() {
        return Employee.TABLE_NAME;
    }

    @Override
    public String getSqlTableCreator() {
        return Employee.getSqlTableCreator();
    }

    public EmployeeRepository(JdbcTemplate jdbcTemplate,
                              NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                              BaseSequenceService<Employee, EmployeeDto> employeeRetryHandler) {
        super(jdbcTemplate, namedParameterJdbcTemplate, employeeRetryHandler);
    }

}
