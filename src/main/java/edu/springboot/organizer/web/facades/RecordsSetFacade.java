package edu.springboot.organizer.web.facades;

import edu.springboot.organizer.data.models.MonthRecord;
import edu.springboot.organizer.data.models.RecordsSet;
import edu.springboot.organizer.utils.CollectionUtils;
import edu.springboot.organizer.utils.DateUtils;
import edu.springboot.organizer.web.dtos.EmployeeDto;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.dtos.RecordsSetDto;
import edu.springboot.organizer.web.dtos.UserDto;
import edu.springboot.organizer.web.exceptions.ControllerException;
import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.mappers.mv.MonthRecordMVMapper;
import edu.springboot.organizer.web.mappers.mv.RecordsSetMVMapper;
import edu.springboot.organizer.web.services.MonthRecordService;
import edu.springboot.organizer.web.services.RecordsSetService;
import edu.springboot.organizer.web.services.UserService;
import edu.springboot.organizer.web.wrappers.MonthRecordMV;
import edu.springboot.organizer.web.wrappers.RecordsSetMV;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@DependsOn(RecordsSetService.BEAN_NAME)
public class RecordsSetFacade {

    private final RecordsSetService recordsSetService;

    private final MonthRecordService monthRecordService;

    private final UserService userService;

    @Value("${endpoint.form-date}")
    private String formRedirect;

    @Value("${endpoint.view-date}")
    private String viewRedirect;

    @Value("${standard.hours.value}")
    private String standardHoursValue;

    public List<RecordsSetMV> getRecordsSets(Date date) {
        date = checkDate(date);
        Calendar calendar = DateUtils.getCalendarFromDate(date);
        int year = calendar.get(Calendar.YEAR);
        int monthNo = calendar.get(Calendar.MONTH) + 1;
        List<RecordsSetDto> recordsSetDtos;
        recordsSetDtos = findRecordsSets(monthNo, year);
        if (!CollectionUtils.isEmpty(recordsSetDtos)) {
            recordsSetDtos.forEach(setDto -> setDto.getMonthRecords().forEach(monthDto -> {
                if (CollectionUtils.isEmpty(monthDto.getDateCells())) {
                    throw new ResultNotFoundException("DateCells list is null or empty! MonthRecord ID: " + monthDto.getCreated());
                }
            }));
            return recordsSetDtos.stream().map(RecordsSetMVMapper::toMV).collect(Collectors.toList());
        }
        return createRecordsSets(monthNo, year).stream().map(RecordsSetMVMapper::toMV).collect(Collectors.toList());
    }

    public RecordsSetMV updateRecordsSet(Date date, RecordsSetMV recordsSetMv) {
        Date finalDate = checkDate(date);
        Calendar calendar = DateUtils.getCalendarFromDate(finalDate);
        Integer year = calendar.get(Calendar.YEAR);
        Integer monthNo = calendar.get(Calendar.MONTH) + 1;
        if (recordsSetMv == null) {
            throw new ResultNotFoundException("RecordsSet date not found!");
        }
        RecordsSetDto recordsSetDto = RecordsSetMVMapper.toDto(recordsSetMv);
        if (!year.equals(recordsSetDto.getYear()) || !monthNo.equals(recordsSetDto.getMonth())) {
            log.error("RecordsSet [{}] date [{}-{}] is not valid! [{}]", recordsSetDto.getCreated(), recordsSetDto.getYear(), recordsSetDto.getDisplayMonthNo(), date);
            throw new ResultNotFoundException("RecordsSet date is not valid!");
        }
        return RecordsSetMVMapper.toMV(updateRecordsSet(recordsSetDto));
    }

    public RecordsSetMV updateMonthRecord(MonthRecordMV monthRecordMV) {
        if (monthRecordMV == null) {
            throw new ResultNotFoundException("Month Record not found!");
        }
        MonthRecordDto monthRecordDto = MonthRecordMVMapper.toDto(monthRecordMV);
        monthRecordService.updateMonthRecordDto(monthRecordDto);
        RecordsSetDto recordsSetDto = recordsSetService.getRecordsSetById(monthRecordDto.getSetId());
        if (recordsSetDto == null) {
            throw new ResultNotFoundException("RecordsSet  not found! " + monthRecordDto.getSetId());
        }
        return RecordsSetMVMapper.toMV(recordsSetDto);
    }

    public String getMonthName(Date date) {
        checkDate(date);
        Calendar calendar = DateUtils.getCalendarFromDate(date);
        int monthNo = calendar.get(Calendar.MONTH) + 1;
        Month month = Month.of(monthNo);
        return monthRecordService.getDateMonthGenerator().getMonthName(month);
    }

    public String getMonthName(int monthNo) {
        Month month = Month.of(monthNo);
        return monthRecordService.getDateMonthGenerator().getMonthName(month);
    }

    public List<RecordsSetDto> findRecordsSets(int month, int year) {
        String userId = getCtxUser() != null ? getCtxUser().getCreated() : "";
        return recordsSetService.getMonthRecordByMonthYearUser(month, year, userId);
    }

    public RecordsSetDto findRecordsSets(String id) {
        return recordsSetService.getRecordsSetById(id);
    }

    public MonthRecordMV findMonthRecordById(String id) {
        MonthRecordDto monthRecordDto = monthRecordService.getMonthRecordById(id);
        if (monthRecordDto != null) {
            return MonthRecordMVMapper.toMV(monthRecordDto);
        }
        return null;
    }

    public String getFormRedirect() {
        if (formRedirect != null) {
            return formRedirect;
        }
        throw new ControllerException("Path variable not found!");
    }

    public Date getSearchDate(String stringDate) {
        try {
            return DateUtils.stringToDate(stringDate, DateUtils.YEAR_MONTH_PATTERN);
        } catch (Exception e) {
            log.error("Incorrect search date [{}]", stringDate, e);
            throw new ControllerException("Incorrect search date!");
        }
    }

    public String getFromSearchDate(Date lookDate) {
        try {
            return DateUtils.getStringFromDate(lookDate, DateUtils.YEAR_MONTH_PATTERN);
        } catch (Exception e) {
            log.error("Incorrect search date [{}]", lookDate, e);
            throw new ControllerException("Incorrect search date!");
        }
    }

    public String getViewRedirect() {
        if (viewRedirect != null) {
            return viewRedirect;
        }
        throw new ControllerException("Path variable not found!");
    }

    public void addNewMonthRecordDto(RecordsSetMV setDto) {
        createMonthRecords(RecordsSetMVMapper.toDto(setDto));
    }

    public void deleteMonthRecordById(String monthRecordId) {
        monthRecordService.deleteMonthRecord(monthRecordId);
    }

    public String getLookDateFromMonthRecordById(String monthRecordId) {
        MonthRecordDto monthRecordDto = monthRecordService.getMonthRecordById(monthRecordId);
        String[] result = new String[1];
        monthRecordDto.getDateCells().stream()
                .filter(Objects::nonNull)
                .findFirst().ifPresent(it ->
                        {
                            String input = it.getDate();
                            int firstIndex = input.indexOf("-");
                            int secondIndex = input.indexOf("-", firstIndex + 1);
                            result[0] = (input.substring(0, secondIndex));
                        }
                );
        return result[0];
    }


    private Date checkDate(Date date) {
        if (date == null && log.isDebugEnabled()) {
            log.debug("No date found. Default set.");
            date = new Date();
        }
        return date;
    }

    private Integer getStandardHoursValue() {
        if (standardHoursValue != null) {
            return Integer.valueOf(standardHoursValue);
        }
        throw new ControllerException("Standard Hours Value not found!");
    }

    private List<RecordsSetDto> createRecordsSets(int month, int year) {
        if (log.isDebugEnabled()) {
            log.debug("Start generating Record Set");
        }
        String userId = getCtxUser() != null ? getCtxUser().getCreated() : "";
        RecordsSetDto recordsSetDto = recordsSetService
                .createRecordsSet(RecordsSet.builder().month(month).year(year).userId(userId).build());
        createMonthRecords(recordsSetDto);
        List<RecordsSetDto> recordsSetDtos = new ArrayList<>();
        recordsSetDtos.add(recordsSetDto);
        return recordsSetDtos;
    }

    private RecordsSetDto updateRecordsSet(RecordsSetDto recordsSetDto) {
        if (log.isDebugEnabled()) {
            log.info("RecordsSetDto [{}] updated", recordsSetDto.getCreated());
        }
        recordsSetService.updateRecordsSet(recordsSetDto);
        return recordsSetDto;
    }

    private void createMonthRecords(RecordsSetDto setDto) {
        MonthRecordDto monthRecordDto = monthRecordService
                .createMonthRecord(MonthRecord.builder()
                        .setId(setDto.getCreated())
                        .employee(EmployeeDto.getDefaultName())
                        .standardHours(getStandardHoursValue())
                        .build(), setDto);
        List<MonthRecordDto> monthRecordDtos = new ArrayList<>();
        monthRecordDtos.add(monthRecordDto);
        setDto.addMonthRecords(monthRecordDtos);
    }

    private UserDto getCtxUser() {
        return userService.getCtxUser();
    }

}
