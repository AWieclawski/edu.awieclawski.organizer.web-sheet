package edu.springboot.organizer.data.daos;

import edu.springboot.organizer.data.daos.base.BaseEntityDao;
import edu.springboot.organizer.data.models.Employee;
import edu.springboot.organizer.data.daos.base.BaseSequenceService;
import edu.springboot.organizer.web.dtos.EmployeeDto;
import edu.springboot.organizer.web.mappers.EmployeeRowMapper;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component(EmployeeDao.BEAN_NAME)
@DependsOn(BaseSequenceService.BEAN_NAME)
public class EmployeeDao extends BaseEntityDao<Employee, EmployeeDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.daos.EmployeeDao";

    public EmployeeDao(BaseSequenceService<Employee, EmployeeDto> idGenerator,
                       JdbcTemplate jdbcTemplate,
                       NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(idGenerator, jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public BaseRowMapper<Employee, EmployeeDto> getBaseRowMapper() {
        return new EmployeeRowMapper();
    }

    @Override
    protected Class<EmployeeDto> getClassDto() {
        return EmployeeDto.class;
    }

    @Override
    protected Class<Employee> getClassEntity() {
        return Employee.class;
    }

    @Override
    public String getTableName() {
        return Employee.TABLE_NAME;
    }

    @Override
    public String getSqlTableCreator() {
        return Employee.getSqlTableCreator();
    }

}
