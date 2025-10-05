package edu.springboot.organizer.web.wrappers;

import edu.springboot.organizer.web.dtos.DateCellDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DateCellsForm implements Serializable {
    private String dateCellFormId;
    private List<DateCellDto> dateCellsList;

    public DateCellsForm(String dateCellFormId) {
        this.dateCellFormId = dateCellFormId;
    }

    public void addDateCell(DateCellDto dto) {
        this.dateCellsList.add(dto);
    }

    public void addDateCells(List<DateCellDto> dtos) {
        this.dateCellsList.addAll(dtos);
    }
}
