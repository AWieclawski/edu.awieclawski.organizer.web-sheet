package edu.springboot.organizer.web.facades;

import edu.springboot.organizer.data.models.MonthRecord;
import edu.springboot.organizer.generator.services.DateMonthGenerator;
import edu.springboot.organizer.utils.CollectionUtils;
import edu.springboot.organizer.utils.DateUtils;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.dtos.EmployeeDto;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.dtos.UserDto;
import edu.springboot.organizer.web.services.MonthRecordService;
import edu.springboot.organizer.web.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@DependsOn(MonthRecordService.BEAN_NAME)
public class MonthRecordsFacade {

    private final DateMonthGenerator dateMonthGenerator;

    private final MonthRecordService monthRecordService;

    private final UserService userService;

    public List<MonthRecordDto> getMonthRecords(Date date) {
        date = checkDate(date);
        Calendar calendar = DateUtils.getCalendarFromDate(date);
        int year = calendar.get(Calendar.YEAR);
        int monthNo = calendar.get(Calendar.MONTH) + 1;
        List<MonthRecordDto> monthRecordDtos;
        monthRecordDtos = findMonthRecords(monthNo, year);
        if (!CollectionUtils.isEmpty(monthRecordDtos)) {
            return monthRecordDtos;
        }
        return createMonthRecords(monthNo, year);
    }

    public String getMonthName(Date date) {
        checkDate(date);
        Calendar calendar = DateUtils.getCalendarFromDate(date);
        int monthNo = calendar.get(Calendar.MONTH) + 1;
        java.time.Month month = java.time.Month.of(monthNo);
        return dateMonthGenerator.getMonthName(month);
    }

    private Date checkDate(Date date) {
        if (date == null) {
            log.info("No date found. Default set.");
            date = new Date();
        }
        return date;
    }

    public List<MonthRecordDto> findMonthRecords(int month, int year) {
        String userId = getCtxUser() != null ? getCtxUser().getCreated() : "";
        return monthRecordService.getMonthRecordByMonthYearUser(month, year, userId);
    }

    private List<MonthRecordDto> createMonthRecords(int month, int year) {
        String userId = getCtxUser() != null ? getCtxUser().getCreated() : "";
        MonthRecordDto monthRecordDto = monthRecordService
                .createMonthRecord(MonthRecord.builder().month(month).year(year).userId(userId).employee(EmployeeDto.getDefaultName()).build());
        List<DateCellDto> dateCellHoursRangeDtos = dateMonthGenerator.dateCellsGenerate(monthRecordDto);
        monthRecordDto.addDateCellsList(dateCellHoursRangeDtos);
        List<MonthRecordDto> monthRecordDtos = new ArrayList<>();
        monthRecordDtos.add(monthRecordDto);
        return monthRecordDtos;
    }

    private UserDto getCtxUser() {
        return userService.getCtxUser();
    }

}
