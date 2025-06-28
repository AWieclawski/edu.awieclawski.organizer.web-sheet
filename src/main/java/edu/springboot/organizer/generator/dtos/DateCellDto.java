package edu.springboot.organizer.generator.dtos;

import edu.springboot.organizer.generator.dtos.base.BaseDto;
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
    private String cellType;
    private Integer beginHour;
    private Integer endHour;
    private String monthRecordId;
}
