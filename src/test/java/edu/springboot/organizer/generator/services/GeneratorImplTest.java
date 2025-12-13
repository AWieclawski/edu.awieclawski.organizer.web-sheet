package edu.springboot.organizer.generator.services;

import edu.springboot.organizer.generator.exceptions.ValidateMonthException;
import edu.springboot.organizer.generator.exceptions.ValidateYearException;
import edu.springboot.organizer.utils.ReflectionUtils;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.dtos.RecordsSetDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class GeneratorImplTest {

    private static RecordsSetDto recordsSetDto;
    private static MonthRecordDto monthRecordDto;

    @BeforeAll
    static void setUp() {
        recordsSetDto = RecordsSetDto.builder().month(2).year(2025).created("MONTH_ID").build();
        monthRecordDto = MonthRecordDto.builder().employee("EMP_ID").setId("SET_ID").created("MONTH_ID").build();
    }

    @Test
    void dateCellsGenerate_withValidArgs_returnsOK() {
        DateMonthGenerator inst = new DateMonthGenerator();
        recordsSetDto = RecordsSetDto.builder().month(2).year(2025).created("MONTH_ID").build();
        List<DateCellDto> list = inst.getGeneratedDateCellDtos(monthRecordDto, recordsSetDto.getMonth(), recordsSetDto.getYear());
        Assertions.assertNotNull(list);
        Assertions.assertEquals("2025-02-01", list.get(0).getDate());
    }

    @Test
    void dateCellsGenerate_withNotValidMonth_throwValidateMonthException() {
        DateMonthGenerator inst = new DateMonthGenerator();
        int month = 13;
        ReflectionUtils.setFieldValue(recordsSetDto, "month", month, true);
        Assertions.assertTrue(month > 12 || month < 1);
        Assertions.assertThrows(ValidateMonthException.class, () -> inst.getGeneratedDateCellDtos(monthRecordDto, recordsSetDto.getMonth(), recordsSetDto.getYear()));
    }

    @Test
    void dateCellsGenerate_withNotValidYear_throwValidateYearException() {
        DateMonthGenerator inst = new DateMonthGenerator();
        int year = 1999;
        ReflectionUtils.setFieldValue(recordsSetDto, "year", year, true);
        Assertions.assertTrue(year > 2100 || year < 2000);
        Assertions.assertThrows(ValidateYearException.class, () -> inst.getGeneratedDateCellDtos(monthRecordDto, recordsSetDto.getMonth(), recordsSetDto.getYear()));
    }

}