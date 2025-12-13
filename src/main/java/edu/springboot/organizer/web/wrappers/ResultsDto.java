package edu.springboot.organizer.web.wrappers;

import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.dtos.RecordsSetDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResultsDto {
    private RecordsSetDto recordsSetDto;
    private MonthRecordDto monthRecordDto;
    private Boolean isError;
}
