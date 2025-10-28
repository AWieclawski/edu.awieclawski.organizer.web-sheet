package edu.springboot.organizer.web.mappers.mv;

import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.dtos.RecordsSetDto;
import edu.springboot.organizer.web.wrappers.MonthRecordMV;
import edu.springboot.organizer.web.wrappers.RecordsSetMV;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecordsSetMVMapper {

    public static RecordsSetMV toMV(RecordsSetDto dto) {
        RecordsSetMV mv = new RecordsSetMV();
        mv.setCreated(dto.getCreated());
        mv.setHashId(dto.getHashId());
        mv.setUserId(dto.getUserId());
        mv.setYear(dto.getYear());
        mv.setMonth(dto.getMonth());
        mv.setMonthRecords(toMVs(dto.getMonthRecords()));
        mv.setMonthDisplay(dto.getDisplayMonthNo());
        return mv;
    }

    private static List<MonthRecordMV> toMVs(List<MonthRecordDto> dtos) {
        if (dtos != null) {
            return dtos.stream().map(MonthRecordMVMapper::toMV).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static RecordsSetDto toDto(RecordsSetMV mv) {
        return RecordsSetDto.builder()
                .created(mv.getCreated())
                .hashId(mv.getHashId())
                .userId(mv.getUserId())
                .year(mv.getYear())
                .month(mv.getMonth())
                .monthRecords(toDtos(mv.getMonthRecords()))
                .build();
    }

    private static List<MonthRecordDto> toDtos(List<MonthRecordMV> mvs) {
        if (mvs != null) {
            return mvs.stream().map(MonthRecordMVMapper::toDto).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}
