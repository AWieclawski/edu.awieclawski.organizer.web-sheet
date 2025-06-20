package edu.springboot.organizer.generator.mappers;

import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.generator.dtos.VisitorDto;
import edu.springboot.organizer.utils.DateUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VisitorRowMapper implements RowMapper<VisitorDto> {

    @Override
    public VisitorDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        VisitorDto dto = VisitorDto.builder()
                .name(rs.getString(Visitor.Const.NAME.getColumn()))
                .url(rs.getString(Visitor.Const.URL.getColumn()))
                .ip(rs.getString(Visitor.Const.IP.getColumn()))
                .timestamp(DateUtils.timestampToToString(rs.getTimestamp(Visitor.Const.TIMESTAMP.getColumn())))
                .created(rs.getString(BaseEntity.BaseConst.ID.getColumn()))
                .build();
        dto.hashCode();
        return dto;
    }


}
