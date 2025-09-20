package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.generator.contracts.DateMonthHolder;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class MonthRecordDto extends BaseDto implements DateMonthHolder {
    private final Integer year;
    private final Integer month;
    private final String userId;
    private final String employee;
    private final List<DateCellDto> dateCellsList;

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
            throw new IllegalArgumentException((month + " <- Month value not valid!"));
        }
        this.month = month;
        this.employee = employee;
        this.userId = userId;
        this.dateCellsList = new ArrayList<>();
    }

    @Override
    public String getLocalDate(int day) {
        return DateMonthHolder.buildLocaLDate(day, this.month, this.year);
    }

    public String getMonthNameByLocale(Locale locale) {
        return Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, locale);
    }

    public void addDateCellsList(List<DateCellDto> dateCellHoursRangeDtos) {
        this.dateCellsList.addAll(dateCellHoursRangeDtos);
    }

    public Integer calculateHours() {
        return dateCellsList.stream()
                .map(DateCellDto::getHours)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
    }
}
