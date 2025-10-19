package edu.springboot.organizer.web.mappers.mv;

import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.wrappers.DateCellMV;
import edu.springboot.organizer.web.wrappers.MonthRecordMV;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MonthRecordMVMapper {

    public static MonthRecordMV toMV(MonthRecordDto dto) {
        MonthRecordMV mv = new MonthRecordMV();
        mv.setCreated(dto.getCreated());
        mv.setHashId(dto.getHashId());
        mv.setEmployee(dto.getEmployee());
        mv.setSetId(dto.getSetId());
        mv.setStandardHours(dto.getStandardHours());
        mv.setDateCells(toMVs(dto.getDateCells()));
        mv.setDateCellsSize(dto.dateCellsSize());
        mv.setHoursCellsSize(dto.hoursCellsSize());
        mv.setCalculateHours(dto.calculateHours());
        mv.setCalculateOvertime(dto.calculateOvertime());
        return mv;
    }

    private static List<DateCellMV> toMVs(List<DateCellDto> dtos) {
        if (dtos != null) {
            return dtos.stream().map(DateCellMVMapper::toMV).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static MonthRecordDto toDto(MonthRecordMV mv) {
        return MonthRecordDto.builder()
                .created(mv.getCreated())
                .hashId(mv.getHashId())
                .employee(mv.getEmployee())
                .setId(mv.getSetId())
                .standardHours(mv.getStandardHours())
                .dateCells(toDtos(mv.getDateCells()))
                .build();
    }

    private static List<DateCellDto> toDtos(List<DateCellMV> mvs) {
        if (mvs != null) {
            return mvs.stream().map(DateCellMVMapper::toDto).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
