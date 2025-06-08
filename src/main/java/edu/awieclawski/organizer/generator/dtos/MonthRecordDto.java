package edu.awieclawski.organizer.generator.dtos;

import edu.awieclawski.organizer.generator.dtos.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, of = {})
public class MonthRecordDto extends BaseDto {
    private Integer year;
    private Integer month;
    private List<DateCellDto> dateCellDtos;
    private EmployeeDto employeeDto;
}
