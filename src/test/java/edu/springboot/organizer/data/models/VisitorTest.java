package edu.springboot.organizer.data.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VisitorTest {

    @Test
    void createTable_notThrow() {
        Assertions.assertDoesNotThrow(Visitor::getSqlTableCreator);
    }

}