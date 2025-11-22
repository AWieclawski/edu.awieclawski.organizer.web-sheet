package edu.springboot.organizer.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseDateUtils {

    private static final String PATTERN = "yyyyMMddHHmmssns";

    public static String getBaseTimestampId() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(getBaseFormatter());
    }

    private static DateTimeFormatter getBaseFormatter() {
        return DateTimeFormatter.ofPattern(PATTERN).withZone(ZoneId.systemDefault());
    }
}
