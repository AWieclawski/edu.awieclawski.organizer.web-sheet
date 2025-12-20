package edu.springboot.organizer.generator.dtos;

import edu.springboot.organizer.web.dtos.DateCellDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DateCellDtoTest {

    @Test
    void buildDateCell_withNegativeHours_hasError() {
        DateCellDto dateCellDto = DateCellDto.builder().hours(-1).build();
        dateCellDto.validate();
        Assertions.assertNotNull(dateCellDto.getErrorMessage());
    }

    @Test
    void buildDateCell_withNegativeEndHour_hasError() {
        DateCellDto dateCellDto = DateCellDto.builder().endHour(-1).build();
        dateCellDto.validate();
        Assertions.assertNotNull(dateCellDto.getErrorMessage());
    }

    @Test
    void buildDateCell_withNegativeBeginHour_hasError() {
        DateCellDto dateCellDto = DateCellDto.builder().beginHour(-1).build();
        dateCellDto.validate();
        Assertions.assertNotNull(dateCellDto.getErrorMessage());
    }

}