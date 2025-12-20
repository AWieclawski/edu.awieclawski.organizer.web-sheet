package edu.springboot.organizer.generator.services;

import edu.springboot.organizer.generator.services.bases.BaseDayMonthGenerator;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(value = DateMonthGenerator.BEAN_NAME)
public class DateMonthGenerator extends BaseDayMonthGenerator<DateCellDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.generator.services.DateMonthGenerator";

    @Override
    protected List<DateCellDto> dumbListGenerate(int monthNo, int year, String monthRecordId) {
        validateMonth(monthNo);
        validateYear(year);
        Map<Integer, String> weekDays = weekDays(monthNo, year);
        List<DateCellDto> dateCellDtos = new ArrayList<>();
        weekDays.forEach((dayNo, weekDay) ->
                dateCellDtos.add(DateCellDto.builder()
                        .day(dayNo)
                        .weekDay(weekDay)
                        .holiday(isHoliday(weekDay))
                        .date(buildDate(dayNo, monthNo, year))
                        .monthRecordId(monthRecordId)
                        .build()));
        return dateCellDtos;
    }

    public List<DateCellDto> getGeneratedDateCellDtos(MonthRecordDto monthRecordDto, int monthNo, int year) {
        List<DateCellDto> dateCellDtoList = dumbListGenerate(monthNo, year, monthRecordDto.getCreated());
        dateCellDtoList.forEach(DateCellDto::autoUpdate);
        return dateCellDtoList;
    }

    public String getMonthName(Month month) {
        return getLocalDisplayName(month);
    }

    public String buildDate(int day, int monthNo, int year) {
        return year + "-" + String.format("%02d", monthNo) + "-" + String.format("%02d", day);
    }

}
