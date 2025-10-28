package edu.springboot.organizer.web.mappers.mv;

import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.wrappers.DateCellMV;

public class DateCellMVMapper {


    public static DateCellMV toMV(DateCellDto dto) {
        DateCellMV mv = new DateCellMV();
        mv.setCreated(dto.getCreated());
        mv.setHashId(dto.getHashId());
        mv.setDate(dto.getDate());
        mv.setWorkType(dto.getWorkType());
        mv.setDay(dto.getDay());
        mv.setBeginHour(dto.getBeginHour());
        mv.setEndHour(dto.getEndHour());
        mv.setHours(dto.getHours());
        mv.setOvertime(dto.getOvertime());
        mv.setWeekDay(dto.getWeekDay());
        mv.setHoliday(dto.getHoliday());
        mv.setMonthRecordId(dto.getMonthRecordId());
        return mv;
    }

    public static DateCellDto toDto(DateCellMV mv) {
        return DateCellDto.builder()
                .created(mv.getCreated())
                .hashId(mv.getHashId())
                .date(mv.getDate())
                .workType(mv.getWorkType())
                .day(mv.getDay())
                .beginHour(mv.getBeginHour())
                .endHour(mv.getEndHour())
                .endHour(mv.getEndHour())
                .hours(mv.getHours())
                .overtime(mv.getOvertime())
                .weekDay(mv.getWeekDay())
                .holiday(mv.getHoliday())
                .monthRecordId(mv.getMonthRecordId())
                .build();
    }
}
