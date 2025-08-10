package edu.springboot.organizer.web.wrappers;

import edu.springboot.organizer.web.dtos.MonthRecordDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MonthRecordsForms implements Serializable {
    private String monthRecordFormId;
    private List<MonthRecordDto> monthRecordsList;

    public void addMonthRecord(MonthRecordDto dto) {
        this.monthRecordsList.add(dto);
    }

    public void addMonthRecords(List<MonthRecordDto> dtos) {
        this.monthRecordsList.addAll(dtos);
    }
}
