package edu.springboot.organizer.web.services;

import edu.springboot.organizer.data.models.MonthRecord;
import edu.springboot.organizer.data.repositories.MonthRecordRepository;
import edu.springboot.organizer.utils.ReflectionUtils;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
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
@Service(value = MonthRecordService.BEAN_NAME)
@DependsOn(MonthRecordRepository.BEAN_NAME)
public class MonthRecordService extends BaseService<MonthRecord, MonthRecordDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.web.services.MonthRecordService";

    private final MonthRecordRepository monthRecordRepository;

    public MonthRecordService(MonthRecordRepository monthRecordRepository) {
        this.monthRecordRepository = monthRecordRepository;
    }

    @Autowired
    private DateCellService dateCellService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public MonthRecordDto createMonthRecord(MonthRecord monthRecord) {
        MonthRecord entity = insertEntity(monthRecord);
        log.info("Saved [{}]", entity);
        return getRepository().getBaseRowMapper().toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<MonthRecordDto> getAllMonthRecords() {
        try {
            List<MonthRecord> entities = monthRecordRepository.findAll();
            return assignDateCellsToEntities(entities);
        } catch (Exception e) {
            log.error("All MonthRecords not found! {}", e.getMessage());
        }
        throw new ResultNotFoundException("All MonthRecords found failed!");
    }

    @Transactional(readOnly = true)
    public MonthRecordDto getMonthRecordById(String id) {
        try {
            MonthRecord entity = monthRecordRepository.findById(id);
            MonthRecordDto dto = getRowMapper().toDto(entity);
            assignDateCells(dto);
            return dto;
        } catch (Exception e) {
            log.error("MonthRecord [{}] not found! {}", id, e.getMessage());
        }
        throw new ResultNotFoundException("MonthRecord search by id failed!");
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeMonthRecords() {
        purgeEntities(getRepository().getTableName());
    }

    @Transactional(readOnly = true)
    public List<MonthRecordDto> getMonthRecordByUser(String userId) {
        try {
            List<MonthRecord> entities = monthRecordRepository.findMonthRecordByUser(userId);
            return assignDateCellsToEntities(entities);
        } catch (Exception e) {
            log.error("MonthRecords by User [{}] not found! {}", userId, e.getMessage());
        }
        throw new ResultNotFoundException("All MonthRecords search failed!");
    }

    @Transactional(readOnly = true)
    public List<MonthRecordDto> getMonthRecordByMonthYearUser(int month, int year, String userId) {
        try {
            List<MonthRecord> entities = monthRecordRepository.findMonthRecordByMonthYearUser(month, year, userId);
            return assignDateCellsToEntities(entities);
        } catch (Exception e) {
            log.error("MonthRecords by month, year, User [{}|{}|{}] not found! {}", month, year, userId, e.getMessage());
        }
        throw new ResultNotFoundException("MonthRecords by month, year, User search failed!");
    }

    @Override
    public void initTable() {
        initTable(MonthRecord.getSqlTableCreator(), MonthRecord.TABLE_NAME);
    }

    @Override
    protected MonthRecordRepository getRepository() {
        return this.monthRecordRepository;
    }

    @Override
    protected BaseRowMapper<MonthRecord, MonthRecordDto> getRowMapper() {
        return getRepository().getBaseRowMapper();
    }

    private List<MonthRecordDto> assignDateCellsToEntities(List<MonthRecord> entities) {
        List<MonthRecordDto> monthRecordDtos = entities.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
        monthRecordDtos.forEach(this::assignDateCells);
        return monthRecordDtos;
    }

    private void assignDateCells(MonthRecordDto monthRecord) {
        if (monthRecord.getCreated() != null) {
            List<DateCellDto> dateCellDtos = dateCellService.getDateCellsByMonthRecord(monthRecord.getCreated());
            ReflectionUtils.setFieldValue(monthRecord, "dateCellDtos", dateCellDtos, true);
        }
    }
}

