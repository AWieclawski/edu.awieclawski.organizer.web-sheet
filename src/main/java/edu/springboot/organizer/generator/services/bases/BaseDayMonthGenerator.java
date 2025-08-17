package edu.springboot.organizer.generator.services.bases;

import edu.springboot.organizer.generator.exceptions.ValidateMonthException;
import edu.springboot.organizer.generator.exceptions.ValidateYearException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class BaseDayMonthGenerator<T> {

    protected abstract List<T> dumbListGenerate(int monthNo, int year);

    protected void validateMonth(int monthNo) {
        if (monthNo < 1 || monthNo > 12) throw new ValidateMonthException("Month value not valid: " + monthNo);
    }

    protected void validateYear(int year) {
        if (year < 2000 || year > 2100) throw new ValidateYearException("Year value not valid: " + year);
    }

    protected Set<Integer> weekendDays(int month, int year) {
        return IntStream.rangeClosed(1, YearMonth.of(year, month).lengthOfMonth())
                .mapToObj(day -> LocalDate.of(year, month, day))
                .filter(date -> date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                        date.getDayOfWeek() == DayOfWeek.SUNDAY)
                .map(LocalDate::getDayOfMonth)
                .collect(Collectors.toSet());
    }
}
