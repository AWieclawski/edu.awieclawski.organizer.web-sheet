package edu.springboot.organizer.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    public static String getStringFromDate(LocalDate date, String pattern) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        return date.format(format);
    }

    public static Instant stringToInstant(String stringDate, DateTimeFormatter dateFormatter, ZoneId zoneId) {
        LocalDateTime localDateTime = LocalDateTime.parse(stringDate, dateFormatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return zonedDateTime.toInstant();
    }

    public static LocalDateTime stringToLocalDateTime(String stringDate, DateTimeFormatter dateFormatter) {
        return LocalDateTime.parse(stringDate, dateFormatter);
    }

    public static Timestamp stringToTimestamp(String stringDate, String pattern) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date parsedDate = dateFormat.parse(stringDate);
        return new java.sql.Timestamp(parsedDate.getTime());
    }

    public static LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

    public static Timestamp localDateTimeToTimestamp(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime);
    }

    public static Timestamp localDateToTimestamp(LocalDate dateTime) {
        return Timestamp.valueOf(dateTime.atStartOfDay());
    }

    public static Timestamp stringToTimestampFormatted(String timestampAsString, DateTimeFormatter dateFormatter) {
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.from(dateFormatter.parse(timestampAsString));
        } catch (Exception ignore) {
            localDateTime = LocalDate.parse(timestampAsString, dateFormatter).atStartOfDay();
        }
        return Timestamp.valueOf(localDateTime);
    }

    public static String localDateTimeToToString(LocalDateTime dateTime) {
        return dateTime.toString();
    }

    public static String timestampToToString(Timestamp timestamp) {
        return timestamp.toString();
    }
}
