package edu.awieclawski.organizer.generator.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
public class MonthRecordDto {
    private Integer year;
    private Integer month;
    private Integer hashId;
    private List<DateCellDto> dateCellDtos;
    private EmployeeDto employeeDto;
}
