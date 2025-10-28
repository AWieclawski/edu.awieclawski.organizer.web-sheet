package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.enums.WorkType;
import edu.springboot.organizer.web.exceptions.DtoValidationException;
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
    private final String weekDay;
    @Setter
    private Integer hours;
    @Setter
    private Integer beginHour;
    @Setter
    private Integer endHour;
    @Setter
    private Integer overtime;
    private final String date;
    @Setter
    private WorkType workType;

    @Builder
    public DateCellDto(String created, Integer hashId, Integer day, Integer hours, Integer beginHour, Integer endHour, String monthRecordId, Boolean holiday, String weekDay, Integer overtime, String date, WorkType workType) {
        super(created, hashId);
        this.holiday = holiday != null && holiday;
        this.beginHour = handleBeginHour(beginHour);
        this.endHour = handleEndHour(endHour);
        this.hours = handleHours(hours);
        this.overtime = handleOvertime(overtime);
        this.monthRecordId = monthRecordId;
        this.day = day;
        this.weekDay = weekDay;
        this.date = date;
        handleWorkType(workType);
    }

    private Integer handleHours(Integer hours) {
        if (hours != null && hours < 0)
            throw new DtoValidationException("Hours cannot be negative! [" + hours + "]");
        else if (hours != null && hours > 24) {
            throw new DtoValidationException("Hours cannot be greater than 24! [" + hours + "]");
        } else if (beginHour != null && endHour != null && beginHour > endHour) {
            throw new DtoValidationException("Begin Hour cannot be greater than End Hour! [" + beginHour + ">" + endHour + "]");
        } else if (beginHour != null && endHour != null && endHour > beginHour) {
            hours = endHour - beginHour;
        }
        return hours;
    }


    private Integer handleOvertime(Integer overtime) {
        if (overtime != null && overtime < 0)
            throw new DtoValidationException("Overtime cannot be negative! [" + hours + "]");
        else if (overtime != null && overtime > 24) {
            throw new DtoValidationException("Overtime cannot be greater than 24! [" + hours + "]");
        } else if (hours != null && overtime != null && (hours + overtime) > 24) {
            throw new DtoValidationException("Overtime and hours sum cannot be greater than 24! [" + overtime + "+" + hours + "]");
        }
        return overtime;
    }

    private Integer handleBeginHour(Integer beginHour) {
        if (beginHour != null && beginHour < 0)
            throw new DtoValidationException("BeginHour cannot be negative! [" + beginHour + "]");
        else if (beginHour != null && beginHour > 24) {
            throw new DtoValidationException("BeginHour cannot be greater than 24! [" + beginHour + "]");
        } else if (beginHour == null && !holiday) {
            beginHour = 7;
        }
        return beginHour;
    }

    private Integer handleEndHour(Integer endHour) {
        if (endHour != null && endHour < 0) {
            throw new DtoValidationException("EndHour cannot be negative! [" + endHour + "]");
        } else if (endHour != null && endHour > 24) {
            throw new DtoValidationException("EndHour cannot be greater than 24! [" + endHour + "]");
        } else if (endHour == null && !holiday) {
            endHour = 15;
        }
        return endHour;
    }

    private void handleWorkType(WorkType workType) {
        this.workType = workType != null ? workType : WorkType.T;
        if (this.workType != WorkType.T) {
            this.endHour = null;
            this.beginHour = null;
            this.overtime = null;
            this.hours = null;
            if (this.workType != WorkType.L4) {
                this.hours = 8;
            }
        }
    }

}
