package edu.springboot.organizer.generator.mappers;

import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.generator.dtos.DateCellDto;
import edu.springboot.organizer.generator.mappers.base.BaseRowMapper;
import edu.springboot.organizer.utils.DateUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DateCellRowMapper implements BaseRowMapper<DateCell, DateCellDto> {

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

    public DateCellDto toDto(DateCell entity) {
        return DateCellDto.builder()
                .beginHour(entity.getBeginHour())
                .endHour(entity.getEndHour())
                .hours(entity.getHours())
                .cellType(entity.getCellType())
                .localDate(entity.getLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyy")))
                .monthRecordId(entity.getMonthRecordId())
                .created(entity.getId())
                .hashId(entity.hashCode())
                .build();
    }

    public Map<String, Object> toMap(DateCell entity) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), entity.getId());
        parameters.put(DateCell.Const.BEGIN_HOUR.getColumn(), entity.getBeginHour());
        parameters.put(DateCell.Const.END_HOUR.getColumn(), entity.getEndHour());
        parameters.put(DateCell.Const.HOURS.getColumn(), entity.getHours());
        parameters.put(DateCell.Const.CELL_TYPE.getColumn(), entity.getCellType());
        parameters.put(DateCell.Const.DATE.getColumn(), DateUtils.localDateToTimestamp(entity.getLocalDate()));
        parameters.put(DateCell.Const.MONTH_RECORD.getColumn(), entity.getMonthRecordId());
        return parameters;
    }


}
