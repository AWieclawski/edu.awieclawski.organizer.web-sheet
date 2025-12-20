package edu.springboot.organizer.data.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DateCellTest {

    @Test
    void createTable_notThrow() {
        Assertions.assertDoesNotThrow(DateCell::getSqlTableCreator);
    }

}