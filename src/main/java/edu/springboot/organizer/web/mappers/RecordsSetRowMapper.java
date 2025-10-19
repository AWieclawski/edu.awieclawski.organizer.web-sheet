package edu.springboot.organizer.web.mappers;

import edu.springboot.organizer.data.models.RecordsSet;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.RecordsSetDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class RecordsSetRowMapper implements BaseRowMapper<RecordsSet, RecordsSetDto> {

    @Override
    public RecordsSet mapRow(ResultSet rs, int rowNum) throws SQLException {
        return RecordsSet.builder()
                .month(rs.getInt(RecordsSet.Const.MONTH.getColumn()))
                .year(rs.getInt(RecordsSet.Const.YEAR.getColumn()))
                .userId(rs.getString(RecordsSet.Const.USER.getColumn()))
                .id(rs.getString(BaseEntity.BaseConst.ID.getColumn()))
                .build();
    }

    public RecordsSetDto toDto(RecordsSet entity) {
        return RecordsSetDto.builder()
                .month(entity.getMonth())
                .year(entity.getYear())
                .userId(entity.getUserId())
                .created(entity.getId())
                .hashId(entity.hashCode())
                .build();
    }

    public Map<String, Object> toMap(RecordsSet entity) {
        Map<String, Object> parameters = newParameters();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), entity.getId());
        parameters.put(RecordsSet.Const.USER.getColumn(), entity.getUserId());
        parameters.put(RecordsSet.Const.MONTH.getColumn(), entity.getMonth());
        parameters.put(RecordsSet.Const.YEAR.getColumn(), entity.getYear());
        return parameters;
    }

    public Map<String, Object> toMap(RecordsSetDto dto) {
        Map<String, Object> parameters = newParameters();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), dto.getCreated());
        parameters.put(RecordsSet.Const.USER.getColumn(), dto.getUserId());
        parameters.put(RecordsSet.Const.MONTH.getColumn(), dto.getMonth());
        parameters.put(RecordsSet.Const.YEAR.getColumn(), dto.getYear());
        return parameters;
    }

    @Override
    public RecordsSet toEntity(RecordsSetDto dto) {
        return RecordsSet.builder()
                .id(dto.getCreated())
                .userId(dto.getUserId())
                .month(dto.getMonth())
                .year(dto.getYear())
                .build();
    }

}
