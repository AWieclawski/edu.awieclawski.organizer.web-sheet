package edu.springboot.organizer.generator.services;

import edu.springboot.organizer.generator.services.bases.BaseDayMonthGenerator;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component(value = DateMonthGenerator.BEAN_NAME)
public class DateMonthGenerator extends BaseDayMonthGenerator<DateCellDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.generator.services.DateMonthGenerator";

    @Override
    protected List<DateCellDto> dumbListGenerate(int monthNo, int year) {
        validateMonth(monthNo);
        validateYear(year);
        Month month = Month.of(monthNo);
        Set<Integer> weekends = weekendDays(monthNo,year);
        return IntStream.range(1, month.maxLength() + 1)
                .mapToObj(dayNo -> DateCellDto.builder().day(dayNo).holiday(weekends.contains(dayNo)).build())
                .collect(Collectors.toList());
    }

    public List<DateCellDto> dateCellsGenerate(MonthRecordDto monthRecordDto) {
        List<DateCellDto> list = dumbListGenerate(monthRecordDto.getMonth(), monthRecordDto.getYear());
        return list.stream()
                .map(it -> DateCellDto.builder()
                        .monthRecordId(monthRecordDto.getCreated())
                        .day(it.getDay())
                        .holiday(it.getHoliday())
                        .build())
                .collect(Collectors.toList());
    }
}
