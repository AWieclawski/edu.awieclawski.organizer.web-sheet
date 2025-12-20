package edu.springboot.organizer.data.daos.base.dtos;

import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

@Builder
@Getter
@Setter
public class RetryDataDto<S extends BaseEntity, T extends BaseDto> {
    private String baseIdKey;
    private S entity;
    private Function<S, S> insertMethod;
}
