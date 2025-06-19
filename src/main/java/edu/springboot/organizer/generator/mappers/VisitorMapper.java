package edu.springboot.organizer.generator.mappers;

import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.generator.dtos.VisitorDto;
import edu.springboot.organizer.utils.DateUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitorMapper {

    public static VisitorDto toDto(Visitor entity) {
        return VisitorDto.builder()
                .timestamp(DateUtils.localDateTimeToToString(entity.getTimestamp()))
                .ip(entity.getIp())
                .name(entity.getName())
                .url(entity.getUrl())
                .created(entity.getId())
                .hashId(entity.hashCode())
                .build();
    }


    public static Map<String, Object> toMap(Visitor visitor) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(BaseEntity.BaseConst.ID.getColumn(), visitor.getId());
        parameters.put(Visitor.Const.TIMESTAMP.getColumn(), DateUtils.localDateTimeToTimestamp(visitor.getTimestamp()));
        parameters.put(Visitor.Const.URL.getColumn(), visitor.getUrl());
        parameters.put(Visitor.Const.IP.getColumn(), visitor.getIp());
        parameters.put(Visitor.Const.NAME.getColumn(), visitor.getName());
        return parameters;
    }
}
