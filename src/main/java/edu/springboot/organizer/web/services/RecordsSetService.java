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

    private final MonthRecordService monthRecordService;

    public RecordsSetService(RecordsSetRepository recordsSetRepository,
                             MonthRecordService monthRecordService) {
        super();
        this.recordsSetRepository = recordsSetRepository;
        this.monthRecordService = monthRecordService;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RecordsSetDto createRecordsSet(RecordsSet recordsSet) {
        return createEntity(recordsSet);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RecordsSetDto updateRecordsSet(RecordsSetDto recordsSetDto) {
        List<MonthRecordDto> monthRecordDtoList = recordsSetDto.getMonthRecords();
        monthRecordDtoList.forEach(this::monthRecordUpdate);
        return dtoUpdate(recordsSetDto);
    }

    private MonthRecordDto monthRecordUpdate(MonthRecordDto monthRecordDto) {
        return monthRecordService.updateMonthRecordDto(monthRecordDto);
    }

    @Transactional(readOnly = true)
    public List<RecordsSetDto> getAllRecordsSets() {
        List<RecordsSetDto> entities = getAllDtos();
        return assignMonthRecordsToEntities(entities);
    }

    @Transactional(readOnly = true)
    public RecordsSetDto getRecordsSetById(String id) {
        RecordsSetDto dto = getEntityById(id);
        assignMonthRecords(dto);
        return dto;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeRecordsSets() {
        purgeEntities();
    }

    @Transactional(readOnly = true)
    public List<RecordsSetDto> getRecordsSetSetByUserId(String userId) {
        try {
            List<RecordsSet> entities = recordsSetRepository.findRecordsSetByUser(userId);
            List<RecordsSetDto> monthRecordDtos = entities.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
            return assignMonthRecordsToEntities(monthRecordDtos);
        } catch (Exception e) {
            log.error("MonthRecords by User [{}] not found! {}", userId, e.getMessage());
        }
        throw new ResultNotFoundException("All MonthRecords search failed!");
    }

    @Transactional(readOnly = true)
    public List<RecordsSetDto> getMonthRecordByMonthYearUser(int month, int year, String userId) {
        try {
            List<RecordsSet> entities = recordsSetRepository.findRecordsSetByMonthYearUser(month, year, userId);
            List<RecordsSetDto> monthRecordDtos = entities.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
            return assignMonthRecordsToEntities(monthRecordDtos);
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
    protected void setEntityClass() {
        this.entityClass = RecordsSet.class;
    }

    @Override
    protected RecordsSetRepository getRepository() {
        return this.recordsSetRepository;
    }

    @Override
    protected BaseRowMapper<RecordsSet, RecordsSetDto> getRowMapper() {
        return getRepository().getBaseRowMapper();
    }

    private List<RecordsSetDto> assignMonthRecordsToEntities(List<RecordsSetDto> monthRecordDtos) {
        monthRecordDtos.forEach(this::assignMonthRecords);
        return monthRecordDtos;
    }

    private void assignMonthRecords(RecordsSetDto recordsSetDto) {
        if (recordsSetDto.getCreated() != null) {
            List<MonthRecordDto> dateCellDtos = monthRecordService.getMonthRecordBySet(recordsSetDto.getCreated());
            ReflectionUtils.setFieldValue(recordsSetDto, "monthRecords", dateCellDtos, true);
        }
    }

}

