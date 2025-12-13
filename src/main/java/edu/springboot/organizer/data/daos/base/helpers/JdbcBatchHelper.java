package edu.springboot.organizer.data.daos.base.helpers;

import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

interface JdbcBatchHelper<S extends BaseEntity, T extends BaseDto>  {

    SimpleJdbcInsert withBatchSize(Integer batchSize);

    SimpleJdbcInsert withRawMapper(BaseRowMapper<S, T> rowMapper);

    /**
     * Copy of: org.springframework.jdbc.core.simple.AbstractJdbcInsert#setParameterValues(java.sql.PreparedStatement, java.util.List, int...)
     *
     * @param preparedStatement
     * @param values
     * @param columnTypes
     * @throws SQLException
     */
    default void setParameterValuesList(PreparedStatement preparedStatement, List<?> values, @Nullable int... columnTypes)
            throws SQLException {
        int colIndex = 0;
        for (Object value : values) {
            colIndex++;
            if (columnTypes == null || colIndex > columnTypes.length) {
                StatementCreatorUtils.setParameterValue(preparedStatement, colIndex, SqlTypeValue.TYPE_UNKNOWN, value);
            } else {
                StatementCreatorUtils.setParameterValue(preparedStatement, colIndex, columnTypes[colIndex - 1], value);
            }
        }
    }
}
