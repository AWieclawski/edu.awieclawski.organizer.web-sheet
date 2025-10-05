package edu.springboot.organizer.generator.dtos;

import edu.springboot.organizer.web.dtos.DateCellDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DateCellDtoTest {

    @Test
    void buildDateCell_withNegativeHours_throw() {
        Throwable th = Assertions.assertThrows(IllegalArgumentException.class,
                () -> DateCellDto.builder().hours(-1).build());
        Assertions.assertNotNull(th);
    }

    @Test
    void buildDateCell_withNegativeEndHour_throw() {
        Throwable th = Assertions.assertThrows(IllegalArgumentException.class,
                () -> DateCellDto.builder().endHour(-1).build());
        Assertions.assertNotNull(th);
    }

    @Test
    void buildDateCell_withNegativeBeginHour_throw() {
        Throwable th = Assertions.assertThrows(IllegalArgumentException.class,
                () -> DateCellDto.builder().beginHour(-1).build());
        Assertions.assertNotNull(th);
    }

}