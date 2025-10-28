package edu.springboot.organizer.web.wrappers;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(of = "hashId")
public class MonthRecordMV {
    private String created;
    private Integer hashId;
    private String setId;
    private String employee;
    private Integer standardHours;
    private List<DateCellMV> dateCells;
    private Integer dateCellsSize;
    private Integer hoursCellsSize;
    private Integer calculateHours;
    private Integer calculateOvertime;
}
