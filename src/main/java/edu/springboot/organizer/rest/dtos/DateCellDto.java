package edu.springboot.organizer.rest.dtos;

import edu.springboot.organizer.rest.dtos.base.BaseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class DateCellDto extends BaseDto {
    private final String localDate;
    private final String monthRecordId;
    private final String cellType;
    @Setter
    private Integer hours;
    @Setter
    private Integer beginHour;
    @Setter
    private Integer endHour;

    @Builder
    public DateCellDto(String created, Integer hashId, String localDate, Integer hours, String cellType, Integer beginHour, Integer endHour, String monthRecordId) {
        super(created, hashId);
        if (endHour != null && endHour < 0)
            throw new IllegalArgumentException(endHour + " <- EndHour cannot be negative!");
        if (beginHour != null && beginHour < 0)
            throw new IllegalArgumentException(beginHour + " <- BeginHour cannot be negative!");
        if (hours != null && hours < 0)
            throw new IllegalArgumentException(hours + " <- Hours cannot be negative!");
        this.localDate = localDate;
        this.hours = hours;
        this.cellType = cellType;
        this.beginHour = beginHour;
        this.endHour = endHour;
        this.monthRecordId = monthRecordId;
    }
}
