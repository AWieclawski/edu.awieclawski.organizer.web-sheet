package edu.springboot.organizer.generator.services;

import edu.springboot.organizer.generator.exceptions.ValidateMonthException;
import edu.springboot.organizer.generator.exceptions.ValidateYearException;
import edu.springboot.organizer.utils.ReflectionUtils;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class GeneratorImplTest {

    private static MonthRecordDto monthRecordDto;

    @BeforeAll
    static void setUp() {
        monthRecordDto = MonthRecordDto.builder().month(2).year(2025).created("MONTH_ID").build();
    }

    @Test
    void dateCellsGenerate_withValidArgs_returnsOK() {
        DateMonthGenerator inst = new DateMonthGenerator();
        monthRecordDto = MonthRecordDto.builder().month(2).year(2025).created("MONTH_ID").build();
        List<DateCellDto> list = inst.dateCellsGenerate(monthRecordDto);
        Assertions.assertNotNull(list);
        Assertions.assertEquals("01-02-2025", monthRecordDto.getLocalDate(list.get(0).getDay()));
    }

    @Test
    void dateCellsGenerate_withNotValidMonth_throwValidateMonthException() {
        DateMonthGenerator inst = new DateMonthGenerator();
        int month = 13;
        ReflectionUtils.setFieldValue(monthRecordDto, "month", month, true);
        Assertions.assertTrue(month > 12 || month < 1);
        Assertions.assertThrows(ValidateMonthException.class, () -> inst.dateCellsGenerate(monthRecordDto));
    }

    @Test
    void dateCellsGenerate_withNotValidYear_throwValidateYearException() {
        DateMonthGenerator inst = new DateMonthGenerator();
        int year = 1999;
        ReflectionUtils.setFieldValue(monthRecordDto, "year", year, true);
        Assertions.assertTrue(year > 2100 || year < 2000);
        Assertions.assertThrows(ValidateYearException.class, () -> inst.dateCellsGenerate(monthRecordDto));
    }

}