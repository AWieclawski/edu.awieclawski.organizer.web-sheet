package edu.springboot.organizer.web.facades;

import edu.springboot.organizer.data.models.MonthRecord;
import edu.springboot.organizer.generator.enums.CellType;
import edu.springboot.organizer.generator.services.DateMonthGenerator;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.services.MonthRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@DependsOn(MonthRecordService.BEAN_NAME)
public class MonthRecordsFacade {

    private final DateMonthGenerator dateMonthGenerator;

    private final MonthRecordService monthRecordService;

    public MonthRecordDto populateMonthRecords(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        // TODO User secure registry and service
        MonthRecordDto monthRecordDto = monthRecordService.createMonthRecord(MonthRecord.builder()
                .month(LocalDateTime.now().getMonthValue()).year(LocalDateTime.now().getYear())
                .userId(UUID.randomUUID().toString()).build());
        List<DateCellDto> dateCellHoursRangeDtos = dateMonthGenerator.dateCellsGenerate(CellType.HOURS_RANGE.name(), monthRecordDto.getCreated(), month, year);
        List<DateCellDto> dateCellWorkingHoursDtos = dateMonthGenerator.dateCellsGenerate(CellType.WORKING_HOURS.name(), monthRecordDto.getCreated(), month, year);
        List<DateCellDto> dateCellOverTimeHoursDtos = dateMonthGenerator.dateCellsGenerate(CellType.OVER_TIME_HOURS.name(), monthRecordDto.getCreated(), month, year);
        monthRecordDto.addDateCellHoursRangeDtos(dateCellHoursRangeDtos);
        monthRecordDto.addDateCellWorkingHoursDtos(dateCellWorkingHoursDtos);
        monthRecordDto.addDateCellOverTimeHoursDtos(dateCellOverTimeHoursDtos);
        return monthRecordDto;
    }

}
