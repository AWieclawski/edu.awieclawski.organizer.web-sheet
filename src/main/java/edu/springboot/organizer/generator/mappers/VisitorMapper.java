package edu.springboot.organizer.generator.mappers;

import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.generator.dtos.VisitorDto;
import edu.springboot.organizer.utils.DateUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitorMapper {

    public static VisitorDto toDto(Visitor entity) {
        return VisitorDto.builder()
                .timestamp(DateUtils.localDateTimeToToString(entity.getTimestamp()))
                .ip(entity.getIp())
                .name(entity.getName())
                .url(entity.getUrl())
                .id(entity.getId())
                .hashId(entity.hashCode())
                .build();
    }

}
