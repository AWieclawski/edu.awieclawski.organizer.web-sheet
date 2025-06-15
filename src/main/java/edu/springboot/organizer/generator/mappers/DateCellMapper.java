package edu.springboot.organizer.generator.mappers;

import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.generator.dtos.DateCellDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateCellMapper {
    public static DateCellDto toDto(DateCell entity) {
        return DateCellDto.builder()
                .beginHour(entity.getBeginHour())
                .endHour(entity.getEndHour())
                .hours(entity.getHours())
                .cellType(entity.getCellType())
                .localDate(entity.getLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyy")))
                .monthRecordId(entity.getMonthRecordId())
                .created(entity.getId())
                .hashId(entity.hashCode())
                .build();
    }
}
