package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.generator.contracts.DateMonthHolder;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class DateCellDto extends BaseDto implements DateMonthHolder {
    private final Integer day;
    private final Integer month;
    private final Integer year;
    private final String monthRecordId;
    private final String cellType;
    @Setter
    private Integer hours;
    @Setter
    private Integer beginHour;
    @Setter
    private Integer endHour;

    @Builder
    public DateCellDto(String created, Integer hashId, Integer day, Integer month, Integer year, Integer hours, String cellType, Integer beginHour, Integer endHour, String monthRecordId) {
        super(created, hashId);
        if (endHour != null && endHour < 0)
            throw new IllegalArgumentException(endHour + " <- EndHour cannot be negative!");
        if (beginHour != null && beginHour < 0)
            throw new IllegalArgumentException(beginHour + " <- BeginHour cannot be negative!");
        if (hours != null && hours < 0)
            throw new IllegalArgumentException(hours + " <- Hours cannot be negative!");
        this.hours = hours;
        this.cellType = cellType;
        this.beginHour = beginHour;
        this.endHour = endHour;
        this.monthRecordId = monthRecordId;
        this.day = day;
        this.month = month;
        this.year = year;
    }


    @Override
    public String getLocalDate() {
        return DateMonthHolder.buildLocaLDate(day, month, year);
    }
}
