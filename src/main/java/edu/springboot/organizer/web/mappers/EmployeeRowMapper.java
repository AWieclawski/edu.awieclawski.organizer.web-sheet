package edu.springboot.organizer.web.mappers;

import edu.springboot.organizer.data.models.Employee;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.EmployeeDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EmployeeRowMapper implements BaseRowMapper<Employee, EmployeeDto> {

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Employee.builder()
                .name(rs.getString(Employee.Const.NAME.getColumn()))
                .surName(rs.getString(Employee.Const.SURNAME.getColumn()))
                .uniqNick(rs.getString(Employee.Const.NICK.getColumn()))
                .userId(rs.getString(Employee.Const.USER.getColumn()))
                .id(rs.getString(BaseEntity.BaseConst.ID.getColumn()))
                .build();
    }

    public EmployeeDto toDto(Employee entity) {
        return EmployeeDto.builder()
                .name(entity.getName())
                .surName(entity.getSurName())
                .userId(entity.getUserId())
                .uniqNick(entity.getUniqNick())
                .created(entity.getId())
                .hashId(entity.hashCode())
                .build();
    }

    public Map<String, Object> toMap(Employee entity) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), entity.getId());
        parameters.put(Employee.Const.NAME.getColumn(), entity.getName());
        parameters.put(Employee.Const.SURNAME.getColumn(), entity.getSurName());
        parameters.put(Employee.Const.NICK.getColumn(), entity.getUniqNick());
        return parameters;
    }

}
