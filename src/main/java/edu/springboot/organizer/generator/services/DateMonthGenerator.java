package edu.springboot.organizer.generator.services;

import edu.springboot.organizer.generator.services.bases.BaseDayMonthGenerator;
import edu.springboot.organizer.web.dtos.DateCellDto;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.util.List;
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
        return IntStream.range(1, month.maxLength() + 1)
                .mapToObj(dayNo -> DateCellDto.builder().day(dayNo).month(monthNo).year(year).build())
                .collect(Collectors.toList());
    }

    public List<DateCellDto> dateCellsGenerate(String cellType, String monthRecordId, int monthNo, int year) {
        List<DateCellDto> list = dumbListGenerate(monthNo, year);
        return list.stream()
                .map(it -> DateCellDto.builder()
                        .cellType(cellType)
                        .monthRecordId(monthRecordId)
                        .day(it.getDay())
                        .month(it.getMonth())
                        .year(it.getYear())
                        .build())
                .collect(Collectors.toList());
    }
}
