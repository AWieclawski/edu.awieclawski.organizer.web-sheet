package edu.springboot.organizer.web.services;

import edu.springboot.organizer.data.models.Employee;
import edu.springboot.organizer.data.repositories.EmployeeRepository;
import edu.springboot.organizer.web.dtos.EmployeeDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import edu.springboot.organizer.web.services.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service(value = EmployeeService.BEAN_NAME)
@DependsOn(EmployeeRepository.BEAN_NAME)
public class EmployeeService extends BaseService<Employee, EmployeeDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.web.services.EmployeeService";

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        super();
        this.employeeRepository = employeeRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        return createEntity(employeeDto);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployees() {
        return getAllDtos();
    }

    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeById(String id) {
        return getEntityById(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeEmployees() {
        purgeEntities();
    }

    @Override
    public void initTable() {
        initTable(Employee.getSqlTableCreator(), Employee.TABLE_NAME);
    }

    @Override
    protected void setEntityClass() {
        this.entityClass = Employee.class;
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
