package edu.springboot.organizer.web.mappers;

import edu.springboot.organizer.data.models.MonthRecord;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class MonthRecordRowMapper implements BaseRowMapper<MonthRecord, MonthRecordDto> {

    @Override
    public MonthRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MonthRecord.builder()
                .employee(rs.getString(MonthRecord.Const.EMPLOYEE.getColumn()))
                .setId(rs.getString(MonthRecord.Const.SET.getColumn()))
                .standardHours(rs.getInt(MonthRecord.Const.STD_HRS.getColumn()))
                .id(rs.getString(BaseEntity.BaseConst.ID.getColumn()))
                .build();
    }

    @Override
    public MonthRecordDto toDto(MonthRecord entity) {
        return MonthRecordDto.builder()
                .setId(entity.getSetId())
                .standardHours(entity.getStandardHours())
                .employee(entity.getEmployee())
                .created(entity.getId())
                .hashId(entity.hashCode())
                .build();
    }

    @Override
    public Map<String, Object> toMap(MonthRecord entity) {
        Map<String, Object> parameters = newParameters();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), entity.getId());
        parameters.put(MonthRecord.Const.SET.getColumn(), entity.getSetId());
        parameters.put(MonthRecord.Const.EMPLOYEE.getColumn(), entity.getEmployee());
        parameters.put(MonthRecord.Const.STD_HRS.getColumn(), entity.getStandardHours());
        return parameters;
    }

    @Override
    public Map<String, Object> toMap(MonthRecordDto dto) {
        Map<String, Object> parameters = newParameters();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), dto.getCreated());
        parameters.put(MonthRecord.Const.SET.getColumn(), dto.getSetId());
        parameters.put(MonthRecord.Const.EMPLOYEE.getColumn(), dto.getEmployee());
        parameters.put(MonthRecord.Const.STD_HRS.getColumn(), dto.getStandardHours());
        return parameters;
    }

    @Override
    public MonthRecord toEntity(MonthRecordDto dto) {
        return MonthRecord.builder()
                .id(dto.getCreated())
                .setId(dto.getSetId())
                .standardHours(dto.getStandardHours())
                .employee(dto.getEmployee())
                .hashId(dto.hashCode())
                .build();
    }

}
