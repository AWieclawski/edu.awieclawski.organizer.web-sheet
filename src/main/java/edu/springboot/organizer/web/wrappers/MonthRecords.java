package edu.springboot.organizer.web.wrappers;

import edu.springboot.organizer.web.dtos.MonthRecordDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MonthRecords implements Serializable {
    private List<MonthRecordDto> monthRecordsList;

    public void addDateCell(MonthRecordDto dto) {
        this.monthRecordsList.add(dto);
    }
}
