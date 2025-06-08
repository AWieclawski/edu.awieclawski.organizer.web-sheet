package edu.awieclawski.organizer.generator.mappers;

import edu.awieclawski.organizer.data.models.Visitor;
import edu.awieclawski.organizer.generator.dtos.VisitorDto;
import edu.awieclawski.organizer.utils.DateUtils;
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
                .hashId(entity.getHashId())
                .build();
    }

}
