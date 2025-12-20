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
import java.util.Calendar;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    public static String DATE_PATTERN_STANDARD = "yyyy-MM-dd";
    public static String YEAR_MONTH_PATTERN = "yyyy-MM";

    public static String getStringFromLocalDate(LocalDate date, String pattern) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        return date.format(format);
    }

    public static String getStringFromLocalDateTime(LocalDateTime localDateTime) {
        final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime ldt = LocalDateTime.now();
        return ldt.format(ISO_FORMATTER);
    }

    public static String getStringFromDate(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static String getStringFromTimestamp(Timestamp timestamp, String pattern) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        return timestamp.toLocalDateTime().format(format);
    }

    public static Instant stringToInstant(String stringDate, DateTimeFormatter dateFormatter, ZoneId zoneId) {
        LocalDateTime localDateTime = LocalDateTime.parse(stringDate, dateFormatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return zonedDateTime.toInstant();
    }

    public static LocalDateTime stringToLocalDateTime(String stringDate, DateTimeFormatter dateFormatter) {
        if (stringDate != null) {
            return LocalDateTime.parse(stringDate, dateFormatter);
        }
        return null;
    }

    public static LocalDate stringToLocalDate(String stringDate, String pattern) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        Timestamp timestamp = stringToTimestampFormatted(stringDate, dateFormatter);
        return timestampToLocalDate(timestamp);
    }

    public static LocalDate getStandardLocalDate(String stringDate) {
        return stringToLocalDate(stringDate, DATE_PATTERN_STANDARD);
    }

    public static Timestamp stringToTimestamp(String stringDate, String pattern) throws ParseException {
        return new java.sql.Timestamp(stringToDate(stringDate, pattern).getTime());
    }

    public static Date stringToDate(String stringDate, String pattern) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.parse(stringDate);
    }

    public static Timestamp getStandardTimestamp(String date) {
        try {
            return stringToTimestamp(date, DATE_PATTERN_STANDARD);
        } catch (Exception e) {
            return Timestamp.valueOf(LocalDateTime.now());
        }
    }

    public static LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
        if (timestamp != null) {
            return timestamp.toLocalDateTime();
        }
        return null;
    }

    public static LocalDate timestampToLocalDate(Timestamp timestamp) {
        return timestampToLocalDateTime(timestamp).toLocalDate();
    }

    public static Timestamp localDateTimeToTimestamp(LocalDateTime dateTime) {
        if (dateTime != null) {
            return Timestamp.valueOf(dateTime);
        }
        return null;
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
        if (dateTime != null) {
            return dateTime.toString();
        }
        return null;
    }

    public static String timestampToToString(Timestamp timestamp) {
        return timestamp.toString();
    }

    public static Calendar getCalendarFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
