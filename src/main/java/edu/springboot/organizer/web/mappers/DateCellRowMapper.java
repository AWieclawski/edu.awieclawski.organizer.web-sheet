package edu.springboot.organizer.web.mappers;

import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.utils.DateUtils;
import edu.springboot.organizer.web.enums.WorkType;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DateCellRowMapper implements BaseRowMapper<DateCell, DateCellDto> {

    @Override
    public DateCell mapRow(ResultSet rs, int rowNum) throws SQLException {
        return DateCell.builder()
                .beginHour(getInteger(rs, DateCell.Const.BEGIN_HOUR.getColumn()))
                .endHour(getInteger(rs, DateCell.Const.END_HOUR.getColumn()))
                .hours(getInteger(rs, DateCell.Const.HOURS.getColumn()))
                .holiday(rs.getInt(DateCell.Const.HOLIDAY.getColumn()))
                .weekDay(rs.getString(DateCell.Const.WEEK_DAY.getColumn()))
                .overtime(getInteger(rs, DateCell.Const.OVERTIME.getColumn()))
                .localDate(DateUtils.timestampToLocalDate(rs.getTimestamp(DateCell.Const.DATE.getColumn())))
                .workType(rs.getString(DateCell.Const.WORK_TYPE.getColumn()))
                .monthRecordId(rs.getString(DateCell.Const.MONTH_RECORD.getColumn()))
                .id(rs.getString(BaseEntity.BaseConst.ID.getColumn()))
                .build();
    }

    public DateCellDto toDto(DateCell entity) {
        return DateCellDto.builder()
                .beginHour(entity.getBeginHour())
                .holiday(entity.getHoliday() != null && entity.getHoliday() > 0)
                .endHour(entity.getEndHour())
                .hours(entity.getHours())
                .day(entity.getLocalDate().getDayOfMonth())
                .date(DateUtils.getStringFromLocalDate(entity.getLocalDate(), DateUtils.DATE_PATTERN_STANDARD))
                .workType(WorkType.getWorkTypeFromString(entity.getWorkType()))
                .weekDay(entity.getWeekDay())
                .overtime(entity.getOvertime())
                .monthRecordId(entity.getMonthRecordId())
                .created(entity.getId())
                .hashId(entity.hashCode())
                .build();
    }

    public Map<String, Object> toMap(DateCell entity) {
        Map<String, Object> parameters = newParameters();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), entity.getId());
        parameters.put(DateCell.Const.BEGIN_HOUR.getColumn(), entity.getBeginHour());
        parameters.put(DateCell.Const.END_HOUR.getColumn(), entity.getEndHour());
        parameters.put(DateCell.Const.HOURS.getColumn(), entity.getHours());
        parameters.put(DateCell.Const.WEEK_DAY.getColumn(), entity.getWeekDay());
        parameters.put(DateCell.Const.HOLIDAY.getColumn(), entity.getHoliday());
        parameters.put(DateCell.Const.OVERTIME.getColumn(), entity.getOvertime());
        parameters.put(DateCell.Const.DATE.getColumn(), DateUtils.localDateToTimestamp(entity.getLocalDate()));
        parameters.put(DateCell.Const.WORK_TYPE.getColumn(), entity.getWorkType());
        parameters.put(DateCell.Const.MONTH_RECORD.getColumn(), entity.getMonthRecordId());
        return parameters;
    }

    public Map<String, Object> toMap(DateCellDto dto) {
        Map<String, Object> parameters = newParameters();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), dto.getCreated());
        parameters.put(DateCell.Const.BEGIN_HOUR.getColumn(), dto.getBeginHour());
        parameters.put(DateCell.Const.END_HOUR.getColumn(), dto.getEndHour());
        parameters.put(DateCell.Const.HOURS.getColumn(), dto.getHours());
        parameters.put(DateCell.Const.WEEK_DAY.getColumn(), dto.getWeekDay());
        parameters.put(DateCell.Const.HOLIDAY.getColumn(), dto.getHoliday());
        parameters.put(DateCell.Const.OVERTIME.getColumn(), dto.getOvertime());
        parameters.put(DateCell.Const.DATE.getColumn(), DateUtils.getStandardTimestamp(dto.getDate()));
        parameters.put(DateCell.Const.WORK_TYPE.getColumn(), dto.getWorkType()!= null ? dto.getWorkType().name() : WorkType.T);
        parameters.put(DateCell.Const.MONTH_RECORD.getColumn(), dto.getMonthRecordId());
        return parameters;
    }

    @Override
    public DateCell toEntity(DateCellDto dto) {
        return DateCell.builder().id(dto.getCreated())
                .beginHour(dto.getBeginHour())
                .endHour(dto.getEndHour())
                .hours(dto.getHours())
                .weekDay(dto.getWeekDay())
                .holiday(dto.getHoliday() ? 1 : 0)
                .overtime(dto.getOvertime())
                .localDate(DateUtils.getStandardLocalDate(dto.getDate()))
                .workType(dto.getWorkType()!= null ? dto.getWorkType().name() : WorkType.T.name())
                .monthRecordId(dto.getMonthRecordId())
                .hashId(dto.hashCode())
                .build();
    }

}
