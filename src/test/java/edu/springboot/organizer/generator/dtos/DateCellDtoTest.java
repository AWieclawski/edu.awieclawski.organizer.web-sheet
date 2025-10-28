package edu.springboot.organizer.generator.dtos;

import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.exceptions.DtoValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DateCellDtoTest {

    @Test
    void buildDateCell_withNegativeHours_throw() {
        Throwable th = Assertions.assertThrows(DtoValidationException.class,
                () -> DateCellDto.builder().hours(-1).build());
        Assertions.assertNotNull(th);
    }

    @Test
    void buildDateCell_withNegativeEndHour_throw() {
        Throwable th = Assertions.assertThrows(DtoValidationException.class,
                () -> DateCellDto.builder().endHour(-1).build());
        Assertions.assertNotNull(th);
    }

    @Test
    void buildDateCell_withNegativeBeginHour_throw() {
        Throwable th = Assertions.assertThrows(DtoValidationException.class,
                () -> DateCellDto.builder().beginHour(-1).build());
        Assertions.assertNotNull(th);
    }

}