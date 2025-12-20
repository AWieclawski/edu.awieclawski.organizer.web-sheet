package edu.springboot.organizer.generator.contracts;

public interface DateMonthHolder {

    static String buildLocaLDate(int day, int month, int year) {
        return String.format("%02d-%02d-%4d", day, month, year);
    }

    String getLocalDate(int day);

}
