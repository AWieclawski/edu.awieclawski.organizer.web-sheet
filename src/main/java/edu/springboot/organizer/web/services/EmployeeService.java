package edu.springboot.organizer.web.services;

import edu.springboot.organizer.data.models.Employee;
import edu.springboot.organizer.data.repositories.EmployeeRepository;
import edu.springboot.organizer.web.dtos.EmployeeDto;
import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import edu.springboot.organizer.web.services.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service(value = EmployeeService.BEAN_NAME)
@DependsOn(EmployeeRepository.BEAN_NAME)
public class EmployeeService extends BaseService<Employee, EmployeeDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.web.services.EmployeeService";

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public EmployeeDto createEmployee(Employee employee) {
        Employee entity = insertEntity(employee);
        log.info("Saved [{}|{}]", entity, BEAN_NAME);
        return getRepository().getBaseRowMapper().toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployees() {
        try {
            List<Employee> entities = employeeRepository.findAll();
            BaseRowMapper<Employee, EmployeeDto> rowMapper = employeeRepository.getBaseRowMapper();
            return entities.stream().map(rowMapper::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Employees not found! {}", e.getMessage());
        }
        throw new ResultNotFoundException("All Employees search failed!");
    }

    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeById(String id) {
        try {
            Employee entity = employeeRepository.findById(id);
            return employeeRepository.getBaseRowMapper().toDto(entity);
        } catch (Exception e) {
            log.error("Employee [{}] not found! {}", id, e.getMessage());
        }
        throw new ResultNotFoundException("Employee search by id failed!");
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeEmployees() {
        purgeEntities(getRepository().getTableName());
    }

    @Override
    public void initTable() {
        initTable(Employee.getSqlTableCreator(), Employee.TABLE_NAME);
    }

    @Override
    protected EmployeeRepository getRepository() {
        return this.employeeRepository;
    }

    @Override
    protected BaseRowMapper<Employee, EmployeeDto> getRowMapper() {
        return getRepository().getBaseRowMapper();
    }
}
