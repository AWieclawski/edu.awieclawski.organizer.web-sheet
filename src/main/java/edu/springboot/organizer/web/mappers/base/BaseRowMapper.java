package edu.springboot.organizer.web.mappers.base;

import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public interface BaseRowMapper<S extends BaseEntity, T extends BaseDto> extends RowMapper<S> {

    T toDto(S entity);

    Map<String, Object> toMap(S entity);

    Map<String, Object> toMap(T dto);

    S toEntity(T dto);

    default Map<String, Object> newParameters() { return new HashMap<>();}

    default Integer getInteger(ResultSet rs, String columnLabel) throws SQLException {
        if (rs.getBigDecimal(columnLabel) == null) {
            return null;
        }
        return rs.getInt(columnLabel);
    }
}
