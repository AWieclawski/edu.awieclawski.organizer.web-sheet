package edu.springboot.organizer.generator.services.bases;

import edu.springboot.organizer.generator.exceptions.ValidateMonthException;
import edu.springboot.organizer.generator.exceptions.ValidateYearException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public abstract class BaseDayMonthGenerator<T> {

    @Value("${locale.language.tag}")
    private String localeLanguageTag;

    protected abstract List<T> dumbListGenerate(int monthNo, int year, String id);

    protected void validateMonth(int monthNo) {
        if (monthNo < 1 || monthNo > 12) throw new ValidateMonthException("Month value not valid: " + monthNo);
    }

    protected void validateYear(int year) {
        if (year < 2000 || year > 2100) throw new ValidateYearException("Year value not valid: " + year);
    }

    protected Map<Integer, String> weekDays(int month, int year) {
        Map<Integer, String> map = new HashMap<>();
        for (int i = 1; i <= YearMonth.of(year, month).lengthOfMonth(); i++) {
            map.put(i, getLocalWeekday(LocalDate.of(year, month, i)));
        }
        return map;
    }

    protected boolean isHoliday(String weekDay) {
        return getLocalDisplayName(DayOfWeek.SATURDAY).equals(weekDay) || getLocalDisplayName(DayOfWeek.SUNDAY).equals(weekDay);
    }

    protected String getLocalDisplayName(Month month) {
        return month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag(getLanguageTag()));
    }

    protected String getLanguageTag() {
        if (localeLanguageTag != null) {
            return localeLanguageTag;
        }
        return "en-EN";
    }

    private String getLocalWeekday(LocalDate localDate) {
        return getLocalDisplayName(localDate.getDayOfWeek());
    }

    private String getLocalDisplayName(DayOfWeek dayOfWeek) {
        return dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag(getLanguageTag()));
    }
}
