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
            throw new IllegalArgumentException("EndHour cannot be negative! [" + endHour + "]");
        else if (endHour != null && endHour > 24) {
            throw new IllegalArgumentException("EndHour cannot be greater than 24! [" + endHour + "]");
        }
        if (beginHour != null && beginHour < 0)
            throw new IllegalArgumentException("BeginHour cannot be negative! [" + beginHour + "]");
        else if (beginHour != null && beginHour > 24) {
            throw new IllegalArgumentException("BeginHour cannot be greater than 24! [" + beginHour + "]");
        }
        if (hours != null && hours < 0)
            throw new IllegalArgumentException("Hours cannot be negative! [" + hours + "]");
        else if (hours != null && hours > 24) {
            throw new IllegalArgumentException("Hours cannot be greater than 24! [" + hours + "]");
        }
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
