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
    public void validate() {
        validateBeginHour();
        validateEndHour();
        validateHours();
        validateOvertime();
    }

    @Override
    public void autoUpdate() {
        this.holiday = holiday != null && holiday; // must be first!
        updateWorkType();
        updateEndHour();
        updateBeginHour();
        updateOvertime();
        updateHours();
    }

    // validators

    private void validateHours() {
        if (this.hours != null && this.hours < 0)
            handleErrorMessage("Hours cannot be negative! [" + hours + "]");
        else if (this.hours != null && this.hours > 24) {
            handleErrorMessage("Hours cannot be greater than 24! [" + hours + "]");
        } else if (this.beginHour != null && this.endHour != null && this.beginHour > this.endHour) {
            handleErrorMessage("Begin Hour cannot be greater than End Hour! [" + this.beginHour + ">" + this.endHour + "]");
        }
    }

    private void validateOvertime() {
        if (this.overtime != null && this.overtime < 0)
            handleErrorMessage("Overtime cannot be negative! [" + this.overtime + "]");
        else if (this.overtime != null && this.overtime > 24) {
            handleErrorMessage("Overtime cannot be greater than 24! [" + this.overtime + "]");
        } else if (this.hours != null && this.overtime != null && (this.hours + this.overtime) > 24) {
            handleErrorMessage("Overtime and hours sum cannot be greater than 24! [" + this.overtime + "+" + this.hours + "]");
        }
    }

    private void validateBeginHour() {
        if (this.beginHour != null && this.beginHour < 0)
            handleErrorMessage("BeginHour cannot be negative! [" + this.beginHour + "]");
        else if (this.beginHour != null && this.beginHour > 24) {
            handleErrorMessage("BeginHour cannot be greater than 24! [" + this.beginHour + "]");
        } else if (this.beginHour != null && this.endHour == null) {
            handleErrorMessage(" EndHour cannot be empty if BeginHour has positive value!");
        }
    }

    private void validateEndHour() {
        if (this.endHour != null && this.endHour < 0) {
            handleErrorMessage("EndHour cannot be negative! [" + this.endHour + "]");
        } else if (this.endHour != null && this.endHour > 24) {
            handleErrorMessage("EndHour cannot be greater than 24! [" + this.endHour + "]");
        } else if (this.endHour != null && this.beginHour == null) {
            handleErrorMessage("BeginHour cannot be empty if EndHour has positive value!");
        }
    }

    // updates

    private void updateWorkType() {
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

    private void updateEndHour() {
        if (this.endHour == null && !this.holiday) {
            this.endHour = 15;
        }
    }

    private void updateBeginHour() {
        if (this.beginHour == null && !this.holiday) {
            this.beginHour = 7;
        }
    }

    private void updateOvertime() {
        if (this.overtime != null && this.overtime == 0) {
            this.overtime = null;
        }
    }

    private void updateHours() {
        if (this.beginHour != null && this.endHour != null && this.endHour > this.beginHour) {
            this.hours = this.endHour - this.beginHour;
        } else if (this.beginHour != null && this.beginHour.equals(this.endHour)) {
            this.hours = null;
            this.beginHour = null;
            this.endHour = null;
        }
    }
}
