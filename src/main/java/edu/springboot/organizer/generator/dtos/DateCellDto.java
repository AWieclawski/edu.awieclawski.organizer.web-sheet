package edu.springboot.organizer.generator.dtos;

import edu.springboot.organizer.generator.dtos.base.BaseDto;
import edu.springboot.organizer.generator.enums.CellType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, of = {})
public class DateCellDto extends BaseDto {
    private String localDate;
    private Integer hours;
    private CellType cellType;
    private Integer beginHour;
    private Integer endHour;
    private String monthRecordId;
}
