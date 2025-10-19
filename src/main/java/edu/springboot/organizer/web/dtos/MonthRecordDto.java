package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//@Setter
@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class MonthRecordDto extends BaseDto {
    private String setId;
    private String employee;
    private Integer standardHours;
    private List<DateCellDto> dateCells;

    @Builder
    public MonthRecordDto(String created,
                          Integer hashId,
                          String employee,
                          String setId,
                          Integer standardHours,
                          List<DateCellDto> dateCells) {
        super(created, hashId);
        this.employee = employee;
        this.setId = setId;
        this.standardHours = standardHours;
        this.dateCells = dateCells;
    }

    public int dateCellsSize() {
        return this.dateCells != null ? this.dateCells.size() : 0;
    }

    public int hoursCellsSize() {
        return this.dateCells != null ? (dateCellsSize() * 2) - 1 : 0;
    }

    public void addDateCells(List<DateCellDto> dateCellDtos) {
        if (this.dateCells == null) {
            this.dateCells = new ArrayList<>();
        }
        this.dateCells.addAll(dateCellDtos);
    }

    public Integer calculateHours() {
        return dateCells.stream()
                .map(DateCellDto::getHours)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
    }

    public Integer calculateOvertime() {
        return dateCells.stream()
                .map(DateCellDto::getOvertime)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
    }

}
