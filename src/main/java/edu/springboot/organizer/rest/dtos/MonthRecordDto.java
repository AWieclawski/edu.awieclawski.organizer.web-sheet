package edu.springboot.organizer.rest.dtos;

import edu.springboot.organizer.rest.dtos.base.BaseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class MonthRecordDto extends BaseDto {
    private final Integer year;
    private final Integer month;
    private final String userId;
    private final String employeeId;
    private final List<DateCellDto> dateCellDtos;

    @Builder
    public MonthRecordDto(String created,
                          Integer hashId,
                          Integer year,
                          Integer month,
                          String employeeId,
                          String userId,
                          List<DateCellDto> dateCellDtos) {
        super(created, hashId);
        this.year = year;
        this.month = month;
        this.employeeId = employeeId;
        this.userId = userId;
        this.dateCellDtos = dateCellDtos;
    }

    public String getMonthNameByLocale(Locale locale) {
        return Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, locale);
    }
}
