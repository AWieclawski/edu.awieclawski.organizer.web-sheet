package edu.springboot.organizer.web.services;

import edu.springboot.organizer.data.models.RecordsSet;
import edu.springboot.organizer.data.repositories.MonthRecordRepository;
import edu.springboot.organizer.data.repositories.RecordsSetRepository;
import edu.springboot.organizer.utils.ReflectionUtils;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.dtos.RecordsSetDto;
import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import edu.springboot.organizer.web.services.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service(value = RecordsSetService.BEAN_NAME)
@DependsOn(MonthRecordRepository.BEAN_NAME)
public class RecordsSetService extends BaseService<RecordsSet, RecordsSetDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.web.services.RecordsSetService";

    private final RecordsSetRepository recordsSetRepository;

    public RecordsSetService(RecordsSetRepository recordsSetRepository) {
        this.recordsSetRepository = recordsSetRepository;
    }

    @Autowired
    private MonthRecordService monthRecordService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RecordsSetDto createRecordsSet(RecordsSet monthRecord) {
        RecordsSet entity = insertEntity(monthRecord);
        log.info("Saved [{}|{}]", entity, BEAN_NAME);
        return getRepository().getBaseRowMapper().toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<RecordsSetDto> getAllRecordsSets() {
        try {
            List<RecordsSet> entities = recordsSetRepository.findAll();
            return assignMonthRecordsToEntities(entities);
        } catch (Exception e) {
            log.error("All RecordsSets not found! {}", e.getMessage());
        }
        throw new ResultNotFoundException("All MonthRecords found failed!");
    }

    @Transactional(readOnly = true)
    public RecordsSetDto getRecordsSetById(String id) {
        try {
            RecordsSet entity = recordsSetRepository.findById(id);
            RecordsSetDto dto = getRowMapper().toDto(entity);
            assignMonthRecords(dto);
            return dto;
        } catch (Exception e) {
            log.error("RecordsSet [{}] not found! {}", id, e.getMessage());
        }
        throw new ResultNotFoundException("MonthRecord search by id failed!");
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeRecordsSets() {
        purgeEntities(getRepository().getTableName());
    }

    @Transactional(readOnly = true)
    public List<RecordsSetDto> getRecordsSetSetByUserId(String userId) {
        try {
            List<RecordsSet> entities = recordsSetRepository.findRecordsSetByUser(userId);
            return assignMonthRecordsToEntities(entities);
        } catch (Exception e) {
            log.error("MonthRecords by User [{}] not found! {}", userId, e.getMessage());
        }
        throw new ResultNotFoundException("All MonthRecords search failed!");
    }

    @Transactional(readOnly = true)
    public List<RecordsSetDto> getMonthRecordByMonthYearUser(int month, int year, String userId) {
        try {
            List<RecordsSet> entities = recordsSetRepository.findRecordsSetByMonthYearUser(month, year, userId);
            return assignMonthRecordsToEntities(entities);
        } catch (Exception e) {
            log.error("RecordsSetDto by month, year, User [{}|{}|{}] not found! {}", month, year, userId, e.getMessage());
        }
        throw new ResultNotFoundException("RecordsSetDto by month, year, User search failed!");
    }

    @Override
    public void initTable() {
        initTable(RecordsSet.getSqlTableCreator(), RecordsSet.TABLE_NAME);
    }

    @Override
    protected RecordsSetRepository getRepository() {
        return this.recordsSetRepository;
    }

    @Override
    protected BaseRowMapper<RecordsSet, RecordsSetDto> getRowMapper() {
        return getRepository().getBaseRowMapper();
    }

    private List<RecordsSetDto> assignMonthRecordsToEntities(List<RecordsSet> entities) {
        List<RecordsSetDto> monthRecordDtos = entities.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
        monthRecordDtos.forEach(this::assignMonthRecords);
        return monthRecordDtos;
    }

    private void assignMonthRecords(RecordsSetDto recordsSetDto) {
        if (recordsSetDto.getCreated() != null) {
            List<MonthRecordDto> dateCellDtos = monthRecordService.getMonthRecordBySet(recordsSetDto.getCreated());

            ReflectionUtils.setFieldValue(recordsSetDto, "monthRecordDtoList", dateCellDtos, true);
        }
    }

}

