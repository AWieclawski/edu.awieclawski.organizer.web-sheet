package edu.springboot.organizer.data.repositories.base;

import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.dto.RetryDataDto;
import edu.springboot.organizer.data.repositories.handlers.TransactionHandler;
import edu.springboot.organizer.data.utils.BaseDateUtils;
import edu.springboot.organizer.data.utils.BaseStringUtils;
import edu.springboot.organizer.data.utils.ReflectionUtils;
import edu.springboot.organizer.generator.dtos.base.BaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service(BaseSequenceService.BEAN_NAME)
@RequiredArgsConstructor
public class BaseSequenceService<S extends BaseEntity, T extends BaseDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.base.BaseSequenceService";

    private static final int MAX_TRY_NUMBER = 3;

    private final TransactionHandler transactionHandler;

    public S handleEntityInn(RetryDataDto<S, T> retryDataDto) {
        return transactionHandler.runInNewTransactionBiFunction(this::handleEntityInn, retryDataDto, 0);
    }

    /**
     * Catch only DataAccessException from
     * org.springframework.jdbc.core.JdbcTemplate.update(
     * org.springframework.jdbc.core.PreparedStatementCreator,
     * org.springframework.jdbc.core.PreparedStatementSetter)
     * and retry with incremented id
     *
     * @param retryDataDto
     * @param count
     * @return
     */
    private S handleEntityInn(RetryDataDto<S, T> retryDataDto, int count) {
        S entity = retryDataDto.getEntity();
        assignTimestampId(entity);
        Map<String, Object> parameters = retryDataDto.getRowMapper().toMap(entity);
        try {
            retryDataDto.getInsertMethod().apply(parameters, entity);
        } catch (DataAccessException e) {
            log.warn("[{}] Inserting Entity [{}] failed {} | {}", ++count, entity.getClass().getSimpleName(), parameters, e.getMessage());
            if (count <= MAX_TRY_NUMBER) {
                incrementId(entity, retryDataDto.getBaseIdKey());
                retryDataDto.setEntity(entity);
                handleEntityInn(retryDataDto, count);
            }
        }
        return null;
    }

    private void incrementId(S entity, String baseIdKey) {
        String timeStampId = entity.getId();
        timeStampId = BaseStringUtils.replaceLastDigitsIncreasedByOne(timeStampId);
        ReflectionUtils.setFieldValue(entity, baseIdKey, timeStampId, true);
        entity.setId(timeStampId);
    }

    private void assignTimestampId(S entity) {
        String timeStampId = entity.getId() != null ? entity.getId() : BaseDateUtils.getBaseTimestampId();
        entity.setId(timeStampId);
    }

}
