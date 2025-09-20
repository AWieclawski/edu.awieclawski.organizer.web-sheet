package edu.springboot.organizer.generator.services.bases;

import edu.springboot.organizer.generator.exceptions.ValidateMonthException;
import edu.springboot.organizer.generator.exceptions.ValidateYearException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseDayMonthGenerator<T> {

    protected abstract List<T> dumbListGenerate(int monthNo, int year);

    protected void validateMonth(int monthNo) {
        if (monthNo < 1 || monthNo > 12) throw new ValidateMonthException("Month value not valid: " + monthNo);
    }

    protected void validateYear(int year) {
        if (year < 2000 || year > 2100) throw new ValidateYearException("Year value not valid: " + year);
    }

    protected Map<Integer, String> weekDays(int month, int year) {
        Map<Integer, String> map = new HashMap<>();
        for (int i = 1; i <= YearMonth.of(year, month).lengthOfMonth(); i++) {
            map.put(i, LocalDate.of(year, month, i).getDayOfWeek().name());
        }
        return map;
    }
}
