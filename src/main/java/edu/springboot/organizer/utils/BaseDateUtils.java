package edu.springboot.organizer.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseDateUtils {

    private static final String PATTERN = "yyyyMMddHHmmssSSS";

    private final static String ZONE_ID = "Europe/Warsaw";

    public static String getBaseTimestampId() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(getBaseFormatter()) + getRandomSuffix();
    }

    private static DateTimeFormatter getBaseFormatter() {
        return DateTimeFormatter.ofPattern(PATTERN).withZone(getZoneIdi());
    }

    private static ZoneId getZoneIdi() {
        return ZoneId.of(ZONE_ID);
    }

    private static int getRandomSuffix() {
        final int MIN_ID = 1000;
        final int MAX_ID = 9999;
        final int RANGE = MAX_ID - MIN_ID + 1;
        return new Random().nextInt(RANGE) + MIN_ID;
    }

}
