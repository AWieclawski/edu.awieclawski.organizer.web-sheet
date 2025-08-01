package edu.springboot.organizer.web.mappers.base;

import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import org.springframework.jdbc.core.RowMapper;

import java.util.Map;

public interface BaseRowMapper<S extends BaseEntity, T extends BaseDto> extends RowMapper<S> {

    T toDto(S entity);

    Map<String, Object> toMap(S entity);
}
