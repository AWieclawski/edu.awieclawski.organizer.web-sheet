package edu.springboot.organizer.data.repositories.handlers;

import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.utils.ReflectionUtils;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.metadata.TableMetaDataContext;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ExtendedJdbcInsert<S extends BaseEntity, T extends BaseDto> extends SimpleJdbcInsert {

    private String insertStringExt;
    private int[] insertTypesExt;

    public ExtendedJdbcInsert(DataSource dataSource) {
        super(dataSource);
    }

    protected TableMetaDataContext tableMetaDataContextExt;

    protected List<String> tableColumnsExt;

    private Class<T> dtoType;

    private Integer batchSize;

    private BaseRowMapper<S, T> rowMapper;

//    public ExtendedJdbcInsert(JdbcTemplate jdbcTemplate) {
//        super(jdbcTemplate);
//    }

    public ExtendedJdbcInsert<S,T> withDtoType(Class<T> entityType) {
        setDtoType(dtoType);
        return this;
    }

    public  ExtendedJdbcInsert<S,T> withBatchSize(Integer batchSize) {
        setBatchSize(batchSize);
        return this;
    }

    public  ExtendedJdbcInsert<S,T> withRawMapper(BaseRowMapper<S, T> rowMapper) {
        setRowMapper(rowMapper);
        return this;
    }

    @Override
    protected void compileInternal() {
        DataSource dataSource = getJdbcTemplate().getDataSource();
        Assert.state(dataSource != null, "No DataSource set");
        this.tableMetaDataContextExt = ReflectionUtils.getFieldValue(this, "tableMetaDataContext", true);
        assert this.tableMetaDataContextExt != null;
        this.tableMetaDataContextExt.processMetaData(dataSource, getColumnNames(), getGeneratedKeyNames());
        this.insertStringExt = this.tableMetaDataContextExt.createInsertString(getGeneratedKeyNames());
        this.insertTypesExt = this.tableMetaDataContextExt.createInsertTypes();
        if (logger.isDebugEnabled()) {
            logger.debug("Compiled insert object: insert string is [" + this.insertStringExt + "]");
        }
        onCompileInternal();
    }

    /**
     * https://medium.com/@AlexanderObregon/bulk-insert-optimization-with-spring-boot-and-jdbc-batching-57dd031ecad8
     *
     * @param dtos
     * @return
     */

    public int[][] executeBatchInsert(List<T> dtos) {

        checkCompiled();

//        this.metaDataProviderExt = ReflectionUtils.getFieldValue(tableMetaDataContextUpd, "metaDataProvider", true);
//        this.tableMetaDataContextExt = ReflectionUtils.getFieldValue(this, "tableMetaDataContext", true);
        assert tableMetaDataContextExt != null;
        this.tableColumnsExt = tableMetaDataContextExt.getTableColumns();
        int[] insertTypes = tableMetaDataContextExt.createInsertTypes();

        final String insertString = getInsertString();

        return getJdbcTemplate().batchUpdate(insertString, dtos, batchSize,
                (ps, dto) -> {
                    Map<String, Object> entityParameters = rowMapper.toMap(dto);
                    List<Object> values = matchInParameterValuesWithInsertColumns(entityParameters);
                    setParameterValues(ps, values, insertTypes);
                });

//        return 1;
    }

    protected void setDtoType(@Nullable Class<T> dtoType) {
        checkIfConfigurationModificationIsAllowed();
        this.dtoType = dtoType;
    }

    protected void setBatchSize(@Nullable Integer batchSize) {
        checkIfConfigurationModificationIsAllowed();
        this.batchSize = batchSize;
    }

    protected void setRowMapper(@Nullable BaseRowMapper<S, T> rowMapper) {
        checkIfConfigurationModificationIsAllowed();
        this.rowMapper = rowMapper;
    }

    private void setParameterValues(PreparedStatement preparedStatement, List<?> values, @Nullable int... columnTypes)
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
