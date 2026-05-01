package edu.springboot.organizer.data.utils;

import edu.springboot.organizer.utils.BaseStringUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BaseStringUtilsTest {

    private final String[] arrayTest = new String[]{
            "2026050112494602401",
            "2026050112494602409",
            "2026050112494602499",
            "2026050112494602999",
            "2026050112494609999",
            "2026050112494600000",
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
