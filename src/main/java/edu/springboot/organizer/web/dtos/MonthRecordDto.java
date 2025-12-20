package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true, of = {})
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MonthRecordDto extends BaseDto {
    private String setId;
    private String employee;
    private Integer standardHours;
    private List<DateCellDto> dateCells;

    public int dateCellsSize() {
        return this.dateCells != null ? this.dateCells.size() : 0;
    }

    public int hoursCellsSize() {
        return this.dateCells != null ? (dateCellsSize() * 2) : 0;
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

    @Override
    public void validate() {
    }

    @Override
    public void autoUpdate() {
    }
}
