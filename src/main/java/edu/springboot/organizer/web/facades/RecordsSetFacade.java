package edu.springboot.organizer.web.facades;

import edu.springboot.organizer.data.models.MonthRecord;
import edu.springboot.organizer.data.models.RecordsSet;
import edu.springboot.organizer.utils.CollectionUtils;
import edu.springboot.organizer.utils.DateUtils;
import edu.springboot.organizer.web.dtos.EmployeeDto;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.dtos.RecordsSetDto;
import edu.springboot.organizer.web.dtos.UserDto;
import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.services.MonthRecordService;
import edu.springboot.organizer.web.services.RecordsSetService;
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
@DependsOn(RecordsSetService.BEAN_NAME)
public class RecordsSetFacade {

    private final RecordsSetService recordsSetService;

    private final MonthRecordService monthRecordService;

    private final UserService userService;

    public List<RecordsSetDto> getRecordsSets(Date date) {
        date = checkDate(date);
        Calendar calendar = DateUtils.getCalendarFromDate(date);
        int year = calendar.get(Calendar.YEAR);
        int monthNo = calendar.get(Calendar.MONTH) + 1;
        List<RecordsSetDto> monthRecordDtos;
        monthRecordDtos = findRecordsSets(monthNo, year);
        if (!CollectionUtils.isEmpty(monthRecordDtos)) {
            monthRecordDtos.forEach(setDto -> setDto.getMonthRecordDtoList().forEach(monthDto -> {
                if (CollectionUtils.isEmpty(monthDto.getDateCellsList())) {
                    throw new ResultNotFoundException("DateCells list is null or empty! MonthRecord ID: " + monthDto.getCreated());
                }
            }));
            return monthRecordDtos;
        }
        return createRecordsSets(monthNo, year);
    }

    public String getMonthName(Date date) {
        checkDate(date);
        Calendar calendar = DateUtils.getCalendarFromDate(date);
        int monthNo = calendar.get(Calendar.MONTH) + 1;
        java.time.Month month = java.time.Month.of(monthNo);
        return monthRecordService.getDateMonthGenerator().getMonthName(month);
    }

    public String getMonthName(int monthNo) {
        java.time.Month month = java.time.Month.of(monthNo);
        return monthRecordService.getDateMonthGenerator().getMonthName(month);
    }

    private Date checkDate(Date date) {
        if (date == null) {
            log.info("No date found. Default set.");
            date = new Date();
        }
        return date;
    }

    public List<RecordsSetDto> findRecordsSets(int month, int year) {
        String userId = getCtxUser() != null ? getCtxUser().getCreated() : "";
        return recordsSetService.getMonthRecordByMonthYearUser(month, year, userId);
    }


    public RecordsSetDto findRecordsSets(String id) {
        return recordsSetService.getRecordsSetById(id);
    }

    private List<RecordsSetDto> createRecordsSets(int month, int year) {
        String userId = getCtxUser() != null ? getCtxUser().getCreated() : "";
        RecordsSetDto recordsSetDto = recordsSetService
                .createRecordsSet(RecordsSet.builder().month(month).year(year).userId(userId).build());
        List<MonthRecordDto> monthRecordDtos = createMonthRecords(recordsSetDto);
        recordsSetDto.addMonthRecords(monthRecordDtos);
        List<RecordsSetDto> recordsSetDtos = new ArrayList<>();
        recordsSetDtos.add(recordsSetDto);
        return recordsSetDtos;
    }

    private List<MonthRecordDto> createMonthRecords(RecordsSetDto setDto) {
        MonthRecordDto monthRecordDto = monthRecordService
                .createMonthRecord(MonthRecord.builder().setId(setDto.getCreated()).employee(EmployeeDto.getDefaultName()).build(), setDto);
        List<MonthRecordDto> monthRecordDtos = new ArrayList<>();
        monthRecordDtos.add(monthRecordDto);
        return monthRecordDtos;
    }

    private UserDto getCtxUser() {
        return userService.getCtxUser();
    }

}
