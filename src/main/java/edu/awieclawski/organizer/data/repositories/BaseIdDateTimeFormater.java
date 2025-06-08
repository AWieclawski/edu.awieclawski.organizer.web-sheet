package edu.awieclawski.organizer.data.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component(value = BaseIdDateTimeFormater.BEAN_NAME)
public class BaseIdDateTimeFormater {

    public static final String BEAN_NAME = "edu.awieclawski.organizer.data.repositories.BaseIdDateTimeFormater";

    @Value("${date.time.format.pattern}")
    private String pattern;

    public DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern(pattern);
    }

}
