package edu.springboot.organizer.data.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CredentialsTest {

    @Test
    void createTable_notThrow() {
        Assertions.assertDoesNotThrow(Credentials::getSqlTableCreator);
    }

}