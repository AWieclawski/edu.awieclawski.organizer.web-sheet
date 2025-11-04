package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.enums.WorkType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true, of = {})
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DateCellDto extends BaseDto {
    private Integer day;
    private String monthRecordId;
    private Boolean holiday;
    private String weekDay;
    private Integer hours;
    private Integer beginHour;
    private Integer endHour;
    private Integer overtime;
    private String date;
    private WorkType workType;

    @Override
    public BaseDto validate() {
        this.holiday = holiday != null && holiday; // must be first!
        this.beginHour = handleBeginHour(beginHour);
        this.endHour = handleEndHour(endHour);
        this.hours = handleHours(hours);
        this.overtime = handleOvertime(overtime);
        handleWorkType(workType);
        return this;
    }

    private Integer handleHours(Integer hours) {
        if (hours != null && hours < 0)
            handleErrorMessage("Hours cannot be negative! [" + hours + "]");
        else if (hours != null && hours > 24) {
            handleErrorMessage("Hours cannot be greater than 24! [" + hours + "]");
        } else if (beginHour != null && endHour != null && beginHour > endHour) {
            handleErrorMessage("Begin Hour cannot be greater than End Hour! [" + beginHour + ">" + endHour + "]");
        } else if (beginHour != null && endHour != null && endHour > beginHour) {
            hours = endHour - beginHour;
        } else if (hours != null && hours == 0) {
            hours = null;
        }
        return hours;
    }

    private Integer handleOvertime(Integer overtime) {
        if (overtime != null && overtime < 0)
            handleErrorMessage("Overtime cannot be negative! [" + overtime + "]");
        else if (overtime != null && overtime > 24) {
            handleErrorMessage("Overtime cannot be greater than 24! [" + overtime + "]");
        } else if (hours != null && overtime != null && (hours + overtime) > 24) {
            handleErrorMessage("Overtime and hours sum cannot be greater than 24! [" + overtime + "+" + hours + "]");
        } else if (overtime != null && overtime == 0) {
            overtime = null;
        }
        return overtime;
    }

    private Integer handleBeginHour(Integer beginHour) {
        if (beginHour != null && beginHour < 0)
            handleErrorMessage("BeginHour cannot be negative! [" + beginHour + "]");
        else if (beginHour != null && beginHour > 24) {
            handleErrorMessage("BeginHour cannot be greater than 24! [" + beginHour + "]");
        } else if (beginHour == null && !holiday) {
            beginHour = 7;
        }
        return beginHour;
    }

    private Integer handleEndHour(Integer endHour) {
        if (endHour != null && endHour < 0) {
            handleErrorMessage("EndHour cannot be negative! [" + endHour + "]");
        } else if (endHour != null && endHour > 24) {
            handleErrorMessage("EndHour cannot be greater than 24! [" + endHour + "]");
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
