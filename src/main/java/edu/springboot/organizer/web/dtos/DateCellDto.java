package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class DateCellDto extends BaseDto {
    private final Integer day;
    private final String monthRecordId;
    private final Boolean holiday;
    @Setter
    private Integer hours;
    @Setter
    private Integer beginHour;
    @Setter
    private Integer endHour;

    @Builder
    public DateCellDto(String created, Integer hashId, Integer day, Integer hours, Integer beginHour, Integer endHour, String monthRecordId, Boolean holiday) {
        super(created, hashId);
        if (beginHour != null && beginHour < 0)
            throw new IllegalArgumentException("BeginHour cannot be negative! [" + beginHour + "]");
        else if (beginHour != null && beginHour > 24) {
            throw new IllegalArgumentException("BeginHour cannot be greater than 24! [" + beginHour + "]");
        } else if (beginHour == null && !holiday) {
            beginHour = 7;
        }
        if (endHour != null && endHour < 0) {
            throw new IllegalArgumentException("EndHour cannot be negative! [" + endHour + "]");
        } else if (endHour != null && endHour > 24) {
            throw new IllegalArgumentException("EndHour cannot be greater than 24! [" + endHour + "]");
        } else if (endHour == null && !holiday) {
            endHour = 15;
        }
        if (hours != null && hours < 0)
            throw new IllegalArgumentException("Hours cannot be negative! [" + hours + "]");
        else if (hours != null && hours > 24) {
            throw new IllegalArgumentException("Hours cannot be greater than 24! [" + hours + "]");
        } else if (beginHour != null && endHour != null && beginHour > endHour) {
            throw new IllegalArgumentException("Begin Hour cannot be greater than End Hour! [" + beginHour + ">" + endHour + "]");
        } else if (beginHour != null && endHour != null && endHour > beginHour) {
            hours = endHour - beginHour;
        }
        this.hours = hours;
        this.beginHour = beginHour;
        this.endHour = endHour;
        this.monthRecordId = monthRecordId;
        this.day = day;
        this.holiday = holiday != null && holiday;
    }

}
