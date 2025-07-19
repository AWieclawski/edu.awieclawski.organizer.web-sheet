package edu.springboot.organizer.generator.services;

import edu.springboot.organizer.generator.exceptions.ValidateMonthException;
import edu.springboot.organizer.generator.exceptions.ValidateYearException;
import edu.springboot.organizer.rest.dtos.DateCellDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class GeneratorImplTest {

    @Test
    void dateCellsGenerate_withValidArgs_returnsOK() {
        DateMonthGenerator inst = new DateMonthGenerator();
        List<DateCellDto> list = inst.dateCellsGenerate("CELL_TYPE", "MONTH_ID", 2, 2025);
        Assertions.assertNotNull(list);
        Assertions.assertEquals("01-02-2025", list.get(0).getLocalDate());
    }

    @Test
    void dateCellsGenerate_withNotValidMonth_throwValidateMonthException() {
        DateMonthGenerator inst = new DateMonthGenerator();
        int month = 13;
        Assertions.assertTrue(month > 12 || month < 1);
        Assertions.assertThrows(ValidateMonthException.class, () -> inst.dateCellsGenerate("CELL_TYPE", "MONTH_ID", month, 2025));
    }

    @Test
    void dateCellsGenerate_withNotValidYear_throwValidateYearException() {
        DateMonthGenerator inst = new DateMonthGenerator();
        int year = 1999;
        Assertions.assertTrue(year > 2100 || year < 2000);
        Assertions.assertThrows(ValidateYearException.class, () -> inst.dateCellsGenerate("CELL_TYPE", "MONTH_ID", 2, year));
    }

}