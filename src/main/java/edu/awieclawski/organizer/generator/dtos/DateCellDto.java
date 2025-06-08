package edu.awieclawski.organizer.generator.dtos;

import edu.awieclawski.organizer.generator.enums.CellType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
public class DateCellDto {
    private String localDate;
    private Integer hours;
    private CellType cellType;
    private Integer beginHour;
    private Integer endHour;
    private Integer hashId;
}
