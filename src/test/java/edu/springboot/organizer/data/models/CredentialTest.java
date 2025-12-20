package edu.springboot.organizer.data.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CredentialTest {

    @Test
    void createTable_notThrow() {
        Assertions.assertDoesNotThrow(Credential::getSqlTableCreator);
    }

}