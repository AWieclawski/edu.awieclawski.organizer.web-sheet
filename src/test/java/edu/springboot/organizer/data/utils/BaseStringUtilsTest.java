package edu.springboot.organizer.data.utils;

import edu.springboot.organizer.utils.BaseStringUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BaseStringUtilsTest {

    private final String[] arrayTest = new String[]{
            "20250615114256129179",
            "20250615114256129399",
            "20250615114256130999",
            "20250615114256139999",
            "20250615221128899999",
            "20250615221129900000",
    };

    @Test
    void increasedByOneStringHasTheSameLength() {
        Arrays.stream(arrayTest).forEach(it -> {
                    String replacement = BaseStringUtils.replaceLastDigitsIncreasedByOne(it);
                    assertEquals(it.length(), replacement.length());
                    assertNotEquals(it, replacement);
                }
        );
    }

    @Test
    void increasedByThreeStringHasTheSameLength() {
        Arrays.stream(arrayTest).forEach(it -> {
                    String replacement = BaseStringUtils.replaceLastDigitsIncreasedByThree(it);
                    assertEquals(it.length(), replacement.length());
                    assertNotEquals(it, replacement);
                }
        );
    }

}