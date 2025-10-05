package edu.springboot.organizer.web.dtos;

import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class MonthRecordDto extends BaseDto {
    private final String setId;
    private final String employee;
    private final List<DateCellDto> dateCellsList;

    @Builder
    public MonthRecordDto(String created,
                          Integer hashId,
                          String employee,
                          String setId) {
        super(created, hashId);
        this.employee = employee;
        this.setId = setId;
        this.dateCellsList = new ArrayList<>();
    }


    public void addDateCellsList(List<DateCellDto> dateCellDtos) {
        this.dateCellsList.addAll(dateCellDtos);
    }

    public Integer calculateHours() {
        return dateCellsList.stream()
                .map(DateCellDto::getHours)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
    }

    public Integer calculateOvertime() {
        return dateCellsList.stream()
                .map(DateCellDto::getOvertime)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
    }

    private Integer handleMonth(Integer month) {
        if (month != null && (month > 12 || month < 1)) {
            throw new IllegalArgumentException((month + " <- Month value not valid!"));
        }
        return month;
    }
}
