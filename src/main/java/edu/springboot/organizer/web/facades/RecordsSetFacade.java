package edu.springboot.organizer.web.facades;

import edu.springboot.organizer.data.models.RecordsSet;
import edu.springboot.organizer.utils.CollectionUtils;
import edu.springboot.organizer.utils.DateUtils;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.dtos.EmployeeDto;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.dtos.RecordsSetDto;
import edu.springboot.organizer.web.dtos.UserDto;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.exceptions.ControllerException;
import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.services.MonthRecordService;
import edu.springboot.organizer.web.services.RecordsSetService;
import edu.springboot.organizer.web.services.UserService;
import edu.springboot.organizer.web.wrappers.ResultsDto;
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

@Slf4j
@Component(RecordsSetFacade.BEAN_NAME)
@RequiredArgsConstructor
@DependsOn(RecordsSetService.BEAN_NAME)
public class RecordsSetFacade {

    public static final String BEAN_NAME = "edu.springboot.organizer.web.facades.RecordsSetFacade";

    private final RecordsSetService recordsSetService;

    private final MonthRecordService monthRecordService;

    private final UserService userService;

    @Value("${endpoint.form-date}")
    private String formRedirect;

    @Value("${endpoint.view-date}")
    private String viewRedirect;

    @Value("${standard.hours.value}")
    private String standardHoursValue;

    public List<RecordsSetDto> getRecordsDtoSets(Date date) {
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
            return recordsSetDtos;
        }
        return createRecordsSets(monthNo, year);
    }

    public RecordsSetDto updateRecordsSet(Date date, RecordsSetDto recordsSetDto) {
        Date finalDate = checkDate(date);
        Calendar calendar = DateUtils.getCalendarFromDate(finalDate);
        Integer year = calendar.get(Calendar.YEAR);
        Integer monthNo = calendar.get(Calendar.MONTH) + 1;
        if (recordsSetDto == null) {
            throw new ResultNotFoundException("RecordsSet date not found!");
        }
        if (!year.equals(recordsSetDto.getYear()) || !monthNo.equals(recordsSetDto.getMonth())) {
            log.error("RecordsSet [{}] date [{}-{}] is not valid! [{}]", recordsSetDto.getCreated(), recordsSetDto.getYear(), recordsSetDto.getDisplayMonthNo(), date);
            throw new ResultNotFoundException("RecordsSet date is not valid!");
        }
        recordsSetDto.getMonthRecords().forEach(monthRecordService::updateMonthRecordDto);
        return recordsSetDto;
    }

    public RecordsSetDto updateMonthRecord(MonthRecordDto monthRecordDto) {
        if (monthRecordDto == null) {
            throw new ResultNotFoundException("Month Record not found!");
        }
        monthRecordService.updateMonthRecordDto(monthRecordDto);
        RecordsSetDto recordsSetDto = recordsSetService.getRecordsSetById(monthRecordDto.getSetId());
        if (recordsSetDto == null) {
            throw new ResultNotFoundException("RecordsSet  not found! " + monthRecordDto.getSetId());
        }
        return recordsSetDto;
    }

    public String getMonthName(Date date) {
        Date finalDate = checkDate(date);
        Calendar calendar = DateUtils.getCalendarFromDate(finalDate);
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

    public MonthRecordDto findMonthRecordById(String id) {
        return monthRecordService.getMonthRecordById(id);
    }

    public MonthRecordDto findMonthRecordDtoById(String id) {
        return monthRecordService.getMonthRecordById(id);
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
        Date finalDate = checkDate(lookDate);
        try {
            return DateUtils.getStringFromDate(finalDate, DateUtils.YEAR_MONTH_PATTERN);
        } catch (Exception e) {
            log.error("Incorrect search date [{}]", finalDate, e);
            throw new ControllerException("Incorrect search date!");
        }
    }

    public String getViewRedirect() {
        if (viewRedirect != null) {
            return viewRedirect;
        }
        throw new ControllerException("Path variable not found!");
    }

    public void addNewMonthRecordDto(RecordsSetDto setDto) {
        createMonthRecordsForRecordSet(setDto);
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

    public ResultsDto getValidatedResults(MonthRecordDto monthRecordDto) {
        boolean isError;
        monthRecordDto.validate();
        monthRecordDto.getDateCells().forEach(DateCellDto::validate);
        isError = monthRecordDto.hasError() || monthRecordDto.getDateCells().stream().anyMatch(BaseDto::hasError);
        return ResultsDto.builder().isError(isError).monthRecordDto(monthRecordDto).build();
    }

    public ResultsDto getValidatedResults(RecordsSetDto setDto) {
        boolean[] isErrorArr = new boolean[1];
        setDto.autoUpdate();
        setDto.validate();
        isErrorArr[0] = setDto.hasError();
        if (!isErrorArr[0]) {
            setDto.getMonthRecords().forEach(monthRecordDto -> {
                boolean isError;
                monthRecordDto.validate();
                monthRecordDto.getDateCells().forEach(DateCellDto::validate);
                isError = monthRecordDto.hasError() || monthRecordDto.getDateCells().stream().anyMatch(BaseDto::hasError);
                if (isError) {
                    isErrorArr[0] = true;
                }
            });
        }
        return ResultsDto.builder().isError(isErrorArr[0]).recordsSetDto(setDto).build();
    }


    private Date checkDate(Date date) {
        if (date == null) {
            if (log.isDebugEnabled()) {
                log.debug("No date found. Default set.");
            }
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
        recordsSetDto.autoUpdate();
        createMonthRecordsForRecordSet(recordsSetDto);
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

    private void createMonthRecordsForRecordSet(RecordsSetDto setDto) {
        MonthRecordDto monthRecordDto = MonthRecordDto.builder()
                .setId(setDto.getCreated())
                .employee(EmployeeDto.getDefaultName())
                .standardHours(getStandardHoursValue())
                .build();
        monthRecordDto.autoUpdate();
        monthRecordService.createMonthRecord(monthRecordDto, setDto);
        monthRecordDto.validate();
        List<MonthRecordDto> monthRecordDtos = new ArrayList<>();
        monthRecordDtos.add(monthRecordDto);
        setDto.addMonthRecords(monthRecordDtos);
    }

    private UserDto getCtxUser() {
        return userService.getCtxUser();
    }

}

