package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class MonthRecordDto extends BaseDto {
    private final Integer year;
    private final Integer month;
    private final String userId;
    private final String employee;
    private final List<DateCellDto> dateCellHoursRangeDtos;
    private final List<DateCellDto> dateCellWorkingHoursDtos;
    private final List<DateCellDto> dateCellOverTimeHoursDtos;

    @Builder
    public MonthRecordDto(String created,
                          Integer hashId,
                          Integer year,
                          Integer month,
                          String employee,
                          String userId) {
        super(created, hashId);
        this.year = year;
        if (month != null && (month > 12 || month < 1)) {
            throw new IllegalArgumentException(month + " <- Month value not valid!");
        }
        this.month = month;
        this.employee = employee;
        this.userId = userId;
        this.dateCellHoursRangeDtos = new ArrayList<>();
        this.dateCellWorkingHoursDtos = new ArrayList<>();
        this.dateCellOverTimeHoursDtos = new ArrayList<>();
    }

    public String getMonthNameByLocale(Locale locale) {
        return Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, locale);
    }

    public void addDateCellHoursRangeDtos(List<DateCellDto> dateCellHoursRangeDtos) {
        this.dateCellHoursRangeDtos.addAll(dateCellHoursRangeDtos);
    }

    public void addDateCellWorkingHoursDtos(List<DateCellDto> dateCellWorkingHoursDtos) {
        this.dateCellWorkingHoursDtos.addAll(dateCellWorkingHoursDtos);
    }

    public void addDateCellOverTimeHoursDtos(List<DateCellDto> dateCellOverTimeHoursDtos) {
        this.dateCellOverTimeHoursDtos.addAll(dateCellOverTimeHoursDtos);
    }


}
