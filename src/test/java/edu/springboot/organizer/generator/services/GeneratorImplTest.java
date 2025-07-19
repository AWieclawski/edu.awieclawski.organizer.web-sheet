package edu.springboot.organizer.generator.services;

import edu.springboot.organizer.rest.dtos.DateCellDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class GeneratorImplTest {

    @Test
    void testMethod() {
        DateMonthGenerator inst = new DateMonthGenerator();
        List<DateCellDto> list = inst.dateCellsGenerate("CELL_TYPE","MONTH_ID",2, 2025);
        Assertions.assertNotNull(list);
        Assertions.assertEquals("01-02-2025", list.get(0).getLocalDate());
    }

}