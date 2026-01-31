package edu.springboot.organizer.data.daos.base;

import edu.springboot.organizer.data.daos.base.dtos.RetryDataDto;
import edu.springboot.organizer.data.daos.base.helpers.TransactionBeansHelper;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.utils.BaseDateUtils;
import edu.springboot.organizer.utils.BaseStringUtils;
import edu.springboot.organizer.utils.ReflectionUtils;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Slf4j
@Service(BaseSequenceService.BEAN_NAME)
@RequiredArgsConstructor
public class BaseSequenceService<S extends BaseEntity, T extends BaseDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.daos.base.BaseSequenceService";

    private static final int MAX_TRY_NUMBER = 3;

    private final TransactionBeansHelper transactionBeansHelper;

    public S handleEntityInn(RetryDataDto<S, T> retryDataDto) {
        return transactionBeansHelper.runInNewTransactionFunction(this::handleEntityRetry, retryDataDto);
    }

    /**
     * Catch only DataAccessException from
     * org.springframework.jdbc.core.JdbcTemplate.update(
     * org.springframework.jdbc.core.PreparedStatementCreator,
     * org.springframework.jdbc.core.PreparedStatementSetter)
     * and retry with incremented id
     *
     * @return S entity
     */
    private S handleEntityRetry(RetryDataDto<S, T> retryDataDto) {
        assignTimestampId(retryDataDto.getEntity());
        int retries = 0;
        while (retries < MAX_TRY_NUMBER) {
            try {
                return retryDataDto.getInsertMethod().apply(retryDataDto.getEntity());
            } catch (DataAccessException e) {
                log.warn("Try attempt No [{}] inserting Entity [{}|{}] failed!", ++retries,
                        retryDataDto.getEntity().getClass().getSimpleName(), retryDataDto.getEntity().getId(), e);
                incrementId(retryDataDto.getEntity(), retryDataDto.getBaseIdKey());
            }
        }
        throw new IllegalStateException(String.format("Task failed after %s attempts", MAX_TRY_NUMBER));
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
