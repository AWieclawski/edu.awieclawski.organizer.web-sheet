package edu.springboot.organizer.rest.mappers;

import edu.springboot.organizer.data.models.MonthRecord;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.rest.dtos.MonthRecordDto;
import edu.springboot.organizer.rest.mappers.base.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MonthRecordRowMapper implements BaseRowMapper<MonthRecord, MonthRecordDto> {

    @Override
    public MonthRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MonthRecord.builder()
                .month(rs.getInt(MonthRecord.Const.MONTH.getColumn()))
                .year(rs.getInt(MonthRecord.Const.YEAR.getColumn()))
                .employeeId(rs.getString(MonthRecord.Const.EMPLOYEE.getColumn()))
                .userId(rs.getString(MonthRecord.Const.USER.getColumn()))
                .id(rs.getString(BaseEntity.BaseConst.ID.getColumn()))
                .build();
    }

    public MonthRecordDto toDto(MonthRecord entity) {
        return MonthRecordDto.builder()
                .month(entity.getMonth())
                .year(entity.getYear())
                .userId(entity.getUserId())
                .employeeId(entity.getEmployeeId())
                .created(entity.getId())
                .hashId(entity.hashCode())
                .build();
    }

    public Map<String, Object> toMap(MonthRecord entity) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), entity.getId());
        parameters.put(MonthRecord.Const.MONTH.getColumn(), entity.getMonth());
        parameters.put(MonthRecord.Const.YEAR.getColumn(), entity.getYear());
        parameters.put(MonthRecord.Const.USER.getColumn(), entity.getUserId());
        parameters.put(MonthRecord.Const.EMPLOYEE.getColumn(), entity.getEmployeeId());
        return parameters;
    }

}
