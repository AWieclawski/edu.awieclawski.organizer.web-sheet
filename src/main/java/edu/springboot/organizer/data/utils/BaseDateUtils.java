package edu.springboot.organizer.data.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseDateUtils {

    private static final String PATTERN = "yyyyMMddHHmmssSSS";

    public static String getBaseTimestampId() {
        LocalDateTime now = LocalDateTime.now();
        String reversed = BaseStringUtils.reverse(String.valueOf(now.getNano()));
        int nano = Integer.parseInt(reversed);
        return now.format(getBaseFormatter()) + nano;
    }

    private static DateTimeFormatter getBaseFormatter() {
        return DateTimeFormatter.ofPattern(PATTERN).withZone(ZoneId.systemDefault());
    }
}
