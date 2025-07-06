package edu.springboot.organizer.data.repositories.base.dto;

import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.rest.dtos.base.BaseDto;
import edu.springboot.organizer.rest.mappers.base.BaseRowMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.function.BiFunction;

@Builder
@Getter
@Setter
public class RetryDataDto<S extends BaseEntity, T extends BaseDto> {
    private String baseIdKey;
    private S entity;
    private BaseRowMapper<S, T> rowMapper;
    private BiFunction<Map<String, Object>, S, S> insertMethod;
}
