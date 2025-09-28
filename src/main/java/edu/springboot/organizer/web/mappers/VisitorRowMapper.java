package edu.springboot.organizer.web.mappers;

import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.utils.DateUtils;
import edu.springboot.organizer.web.dtos.VisitorDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class VisitorRowMapper implements BaseRowMapper<Visitor, VisitorDto> {

    @Override
    public Visitor mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Visitor.builder()
                .name(rs.getString(Visitor.Const.NAME.getColumn()))
                .url(rs.getString(Visitor.Const.URL.getColumn()))
                .ip(rs.getString(Visitor.Const.IP.getColumn()))
                .timestamp(DateUtils.timestampToLocalDateTime(rs.getTimestamp(Visitor.Const.TIMESTAMP.getColumn())))
                .id(rs.getString(BaseEntity.BaseConst.ID.getColumn()))
                .build();
    }

    public VisitorDto toDto(Visitor entity) {
        return VisitorDto.builder()
                .timestamp(DateUtils.localDateTimeToToString(entity.getTimestamp()))
                .ip(entity.getIp())
                .name(entity.getName())
                .url(entity.getUrl())
                .created(entity.getId())
                .hashId(entity.hashCode())
                .build();
    }

    public Map<String, Object> toMap(Visitor entity) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), entity.getId());
        parameters.put(Visitor.Const.TIMESTAMP.getColumn(), DateUtils.localDateTimeToTimestamp(entity.getTimestamp()));
        parameters.put(Visitor.Const.URL.getColumn(), entity.getUrl());
        parameters.put(Visitor.Const.IP.getColumn(), entity.getIp());
        parameters.put(Visitor.Const.NAME.getColumn(), entity.getName());
        return parameters;
    }

    @Override
    public Visitor toEntity(VisitorDto dto) {
        return Visitor.builder().id(dto.getCreated())
                .timestamp(DateUtils.getStandardLocalDate(dto.getTimestamp()).atStartOfDay())
                .url(dto.getUrl())
                .ip(dto.getIp())
                .name(dto.getName())
                .build();
    }

}
