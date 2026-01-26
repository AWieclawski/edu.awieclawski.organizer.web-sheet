package edu.springboot.organizer.web.dtos.base;

import edu.springboot.organizer.web.dtos.CredentialDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BaseDtoTest {

    @Test
    void getErrorList() {
        int iterate = 3;
        CredentialDto testObject = CredentialDto.builder().build();
        for (int i = 0; i < iterate; i++) {
            testObject.addErrorMessage("Error");
        }
        Assertions.assertFalse(testObject.getErrorList().isEmpty());
        Assertions.assertEquals(iterate, testObject.getErrorList().size());
    }
}