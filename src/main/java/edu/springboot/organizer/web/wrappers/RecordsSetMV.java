package edu.springboot.organizer.web.wrappers;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(of = "hashId")
public class RecordsSetMV {
    private String created;
    private Integer hashId;
    private Integer year;
    private Integer month;
    private String userId;
    private List<MonthRecordMV> monthRecords;
    private String monthDisplay;
}
