package edu.springboot.organizer.web.wrappers;

import lombok.Data;

@Data
public class DateCellMV {
    private String created;
    private Integer hashId;
    private Integer day;
    private String monthRecordId;
    private Boolean holiday;
    private String weekDay;
    private String date;
    // variable
    private Integer hours;
    private Integer beginHour;
    private Integer endHour;
    private Integer overtime;
}
