package edu.springboot.organizer.data.repositories.base.dto;

import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
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
    private T dto;
    private BaseRowMapper<S, T> rowMapper;
    private BiFunction<Map<String, Object>, S, S> insertMethod;
}
