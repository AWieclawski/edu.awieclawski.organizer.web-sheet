package edu.springboot.organizer.generator.enums;

import java.util.Arrays;

public enum CellType {
    OVER_TIME_HOURS,
    WORKING_HOURS,
    HOURS_RANGE;

    public static CellType getByName(String name) {
        return Arrays.stream(CellType.values()).filter(it -> it.name().equals(name)).findFirst()
                .orElse(null);
    }
}
