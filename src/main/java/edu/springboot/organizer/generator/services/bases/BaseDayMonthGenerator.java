package edu.springboot.organizer.generator.services.bases;

import edu.springboot.organizer.generator.contracts.DateMonthHolder;
import edu.springboot.organizer.generator.exceptions.ValidateMonthException;
import edu.springboot.organizer.generator.exceptions.ValidateYearException;

import java.util.List;

public abstract class BaseDayMonthGenerator<T extends DateMonthHolder> {

    protected abstract List<T> dumbListGenerate(int monthNo, int year);

    protected void validateMonth(int monthNo) {
        if (monthNo < 1 || monthNo > 12) throw new ValidateMonthException("Month value not valid: " + monthNo);
    }

    protected void validateYear(int year) {
        if (year < 2000 || year > 2100) throw new ValidateYearException("Year value not valid: " + year);
    }
}
