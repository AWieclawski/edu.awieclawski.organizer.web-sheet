package edu.springboot.organizer.generator.services;

import edu.springboot.organizer.generator.services.bases.BaseDayMonthGenerator;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(value = DateMonthGenerator.BEAN_NAME)
public class DateMonthGenerator extends BaseDayMonthGenerator<DateCellDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.generator.services.DateMonthGenerator";

    @Override
    protected List<DateCellDto> dumbListGenerate(int monthNo, int year) {
        validateMonth(monthNo);
        validateYear(year);
        Map<Integer, String> weekDays = weekDays(monthNo, year);
        List<DateCellDto> dateCellDtos = new ArrayList<>();
        weekDays.forEach((dayNo, weekDay) -> {
                    dateCellDtos.add(DateCellDto.builder()
                            .day(dayNo)
                            .weekDay(weekDay)
                            .holiday(isHoliday(weekDay))
                            .build());
                }
        );
        return dateCellDtos;
    }

    public List<DateCellDto> dateCellsGenerate(MonthRecordDto monthRecordDto) {
        List<DateCellDto> list = dumbListGenerate(monthRecordDto.getMonth(), monthRecordDto.getYear());
        return list.stream()
                .map(it -> DateCellDto.builder()
                        .monthRecordId(monthRecordDto.getCreated())
                        .day(it.getDay())
                        .holiday(it.getHoliday())
                        .weekDay(it.getWeekDay())
                        .build())
                .collect(Collectors.toList());
    }
}
