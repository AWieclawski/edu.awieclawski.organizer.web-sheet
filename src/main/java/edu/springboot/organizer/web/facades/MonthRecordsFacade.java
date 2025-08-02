package edu.springboot.organizer.web.facades;

import edu.springboot.organizer.data.models.MonthRecord;
import edu.springboot.organizer.generator.enums.CellType;
import edu.springboot.organizer.generator.services.DateMonthGenerator;
import edu.springboot.organizer.utils.CollectionUtils;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.dtos.EmployeeDto;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.dtos.UserDto;
import edu.springboot.organizer.web.services.MonthRecordService;
import edu.springboot.organizer.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@DependsOn(MonthRecordService.BEAN_NAME)
public class MonthRecordsFacade {

    private final DateMonthGenerator dateMonthGenerator;

    private final MonthRecordService monthRecordService;

    private final UserService userService;

    public List<MonthRecordDto> populateMonthRecords(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        List<MonthRecordDto> monthRecordDtos;
        monthRecordDtos = getMonthRecords(month, year);
        if (!CollectionUtils.isEmpty(monthRecordDtos)) {
            return monthRecordDtos;
        }
        return createMonthRecords(month, year);
    }

    public List<MonthRecordDto> getMonthRecords(int month, int year) {
        String userId = getCtxUser() != null ? getCtxUser().getCreated() : "";
        return monthRecordService.getMonthRecordByMonthYearUser(month, year, userId);
    }

    private List<MonthRecordDto> createMonthRecords(int month, int year) {
        String userId = getCtxUser() != null ? getCtxUser().getCreated() : "";
        MonthRecordDto monthRecordDto = monthRecordService
                .createMonthRecord(MonthRecord.builder().month(month).year(year).userId(userId).employee(EmployeeDto.getDefaultName()).build());
        List<DateCellDto> dateCellHoursRangeDtos = dateMonthGenerator.dateCellsGenerate(CellType.HOURS_RANGE.name(), monthRecordDto.getCreated(), month, year);
        List<DateCellDto> dateCellWorkingHoursDtos = dateMonthGenerator.dateCellsGenerate(CellType.WORKING_HOURS.name(), monthRecordDto.getCreated(), month, year);
        List<DateCellDto> dateCellOverTimeHoursDtos = dateMonthGenerator.dateCellsGenerate(CellType.OVER_TIME_HOURS.name(), monthRecordDto.getCreated(), month, year);
        monthRecordDto.addDateCellHoursRangeDtos(dateCellHoursRangeDtos);
        monthRecordDto.addDateCellWorkingHoursDtos(dateCellWorkingHoursDtos);
        monthRecordDto.addDateCellOverTimeHoursDtos(dateCellOverTimeHoursDtos);
        List<MonthRecordDto> monthRecordDtos = new ArrayList<>();
        monthRecordDtos.add(monthRecordDto);
        return monthRecordDtos;
    }

    private UserDto getCtxUser() {
        return userService.getCtxUser();
    }

}
