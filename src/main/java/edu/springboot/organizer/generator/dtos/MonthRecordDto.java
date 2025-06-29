package edu.springboot.organizer.generator.dtos;

import edu.springboot.organizer.generator.dtos.base.BaseDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true, of = {})
public class MonthRecordDto extends BaseDto {
    private final Integer year;
    private final Integer month;
    private final List<DateCellDto> dateCellDtos;
    private final EmployeeDto employeeDto;

    @Builder
    public MonthRecordDto(String created, Integer hashId, Integer year, Integer month, List<DateCellDto> dateCellDtos, EmployeeDto employeeDto) {
        super(created, hashId);
        this.year = year;
        this.month = month;
        this.dateCellDtos = dateCellDtos;
        this.employeeDto = employeeDto;
    }
}
