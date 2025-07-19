package edu.springboot.organizer.generator.services.bases;

import edu.springboot.organizer.generator.contracts.DateMonthHolder;

import java.util.List;

public abstract class BaseDayMonthGenerator<T extends DateMonthHolder> {

    protected abstract List<T> dumbListGenerate(int monthNo, int year);
}
