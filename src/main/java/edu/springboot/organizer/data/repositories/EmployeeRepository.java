package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.daos.EmployeeDao;
import edu.springboot.organizer.data.models.Employee;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.web.dtos.EmployeeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository(EmployeeRepository.BEAN_NAME)
@DependsOn(EmployeeDao.BEAN_NAME)
public class EmployeeRepository extends BaseRepository<Employee, EmployeeDto> {

    public EmployeeRepository(EmployeeDao employeeDao) {
        super(employeeDao);
    }

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

}
