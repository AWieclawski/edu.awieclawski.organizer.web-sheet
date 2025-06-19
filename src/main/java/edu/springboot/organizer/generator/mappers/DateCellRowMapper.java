package edu.springboot.organizer.generator.mappers;

import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.generator.dtos.DateCellDto;
import edu.springboot.organizer.utils.DateUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DateCellRowMapper implements RowMapper<DateCellDto> {

    @Override
    public DateCellDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        DateCellDto dto = DateCellDto.builder()
                .beginHour(rs.getInt(DateCell.Const.BEGIN_HOUR.getColumn()))
                .endHour(rs.getInt(DateCell.Const.END_HOUR.getColumn()))
                .hours(rs.getInt(DateCell.Const.HOURS.getColumn()))
                .localDate(DateUtils.timestampToToString(rs.getTimestamp(DateCell.Const.DATE.getColumn())))
                .monthRecordId(rs.getString(DateCell.Const.MONTH_RECORD.getColumn()))
                .created(rs.getString(BaseEntity.BaseConst.ID.getColumn()))
                .build();
        dto.hashCode();
        return dto;
    }

}
