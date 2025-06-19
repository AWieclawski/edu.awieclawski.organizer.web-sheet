package edu.springboot.organizer.generator.mappers;

import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.generator.dtos.DateCellDto;
import edu.springboot.organizer.utils.DateUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<String, Object> toMap(DateCell dateCell) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), dateCell.getId());
        parameters.put(DateCell.Const.BEGIN_HOUR.getColumn(), dateCell.getBeginHour());
        parameters.put(DateCell.Const.END_HOUR.getColumn(), dateCell.getEndHour());
        parameters.put(DateCell.Const.HOURS.getColumn(), dateCell.getHours());
        parameters.put(DateCell.Const.CELL_TYPE.getColumn(), dateCell.getCellType());
        parameters.put(DateCell.Const.DATE.getColumn(), DateUtils.localDateToTimestamp(dateCell.getLocalDate()));
        parameters.put(DateCell.Const.MONTH_RECORD.getColumn(), dateCell.getMonthRecordId());
        return parameters;
    }
}
