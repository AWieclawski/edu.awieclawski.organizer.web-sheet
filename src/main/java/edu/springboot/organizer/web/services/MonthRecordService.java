package edu.springboot.organizer.web.services;

import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.models.MonthRecord;
import edu.springboot.organizer.data.repositories.MonthRecordRepository;
import edu.springboot.organizer.generator.services.DateMonthGenerator;
import edu.springboot.organizer.utils.ReflectionUtils;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.dtos.RecordsSetDto;
import edu.springboot.organizer.web.exceptions.QueryException;
import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import edu.springboot.organizer.web.services.base.BaseService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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

    @Getter
    private final DateMonthGenerator dateMonthGenerator;

    private final DateCellService dateCellService;

    public MonthRecordService(MonthRecordRepository monthRecordRepository,
                              DateMonthGenerator dateMonthGenerator,
                              DateCellService dateCellService) {
        super();
        this.monthRecordRepository = monthRecordRepository;
        this.dateMonthGenerator = dateMonthGenerator;
        this.dateCellService = dateCellService;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public MonthRecordDto createMonthRecord(MonthRecord monthRecord, RecordsSetDto setDto) {
        MonthRecordDto monthRecordDto = createEntity(monthRecord);
        List<DateCell> dateCells = dateMonthGenerator.dateCellsGenerate(monthRecordDto, setDto.getMonth(), setDto.getYear());
        List<DateCellDto> dateCellDtos = dateCellService.createDateCells(dateCells);
        monthRecordDto.addDateCells(dateCellDtos);
        return monthRecordDto;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public MonthRecordDto updateMonthRecordDto(MonthRecordDto monthRecordDto) {
        List<DateCellDto> dateCellDtos = monthRecordDto.getDateCells();
        dateCellDtos.forEach(this::dateCellDtoUpdate);
        return dtoUpdate(monthRecordDto);
    }

    private DateCellDto dateCellDtoUpdate(DateCellDto dto) {
        return dateCellService.updateDateCell(dto);
    }

    @Transactional(readOnly = true)
    public List<MonthRecordDto> getAllMonthRecords() {
        List<MonthRecordDto> entities = getAllEntities();
        return assignDateCellsToMonthRecords(entities);
    }

    @Transactional(readOnly = true)
    public MonthRecordDto getMonthRecordById(String id) {
        MonthRecordDto dto = getEntityById(id);
        assignDateCells(dto);
        return dto;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeMonthRecords() {
        purgeEntities();
    }

    @Transactional(readOnly = true)
    public List<MonthRecordDto> getMonthRecordBySet(String setId) {
        try {
            List<MonthRecord> entities = monthRecordRepository.findMonthRecordBySet(setId);
            List<MonthRecordDto> monthRecordDtos = entities.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
            return assignDateCellsToMonthRecords(monthRecordDtos);
        } catch (Exception e) {
            log.error("MonthRecords by Set [{}] not found! | {}", setId, e.getMessage());
        }
        throw new ResultNotFoundException("MonthRecords search failed!");
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Integer deleteMonthRecord(String id) {
        try {
            dateCellService.deleteDateCellsByMonthRecordId(id);
            return deleteEntity(id);
        } catch (Exception e) {
            log.error("MonthRecord delete by id [{}] failed! ", id, e);
        }
        throw new QueryException("MonthRecord delete failed! ");
    }


    @Override
    public void initTable() {
        initTable(MonthRecord.getSqlTableCreator(), MonthRecord.TABLE_NAME);
    }

    @Override
    protected void setEntityClass() {
        this.dtoClass = MonthRecordDto.class;
    }

    @Override
    protected MonthRecordRepository getRepository() {
        return this.monthRecordRepository;
    }

    @Override
    protected BaseRowMapper<MonthRecord, MonthRecordDto> getRowMapper() {
        return getRepository().getBaseRowMapper();
    }

    private List<MonthRecordDto> assignDateCellsToMonthRecords(List<MonthRecordDto> monthRecordDtos) {
        monthRecordDtos.forEach(this::assignDateCells);
        return monthRecordDtos;
    }

    private void assignDateCells(MonthRecordDto monthRecordDto) {
        if (monthRecordDto.getCreated() != null) {
            List<DateCellDto> dateCellDtos = dateCellService.getDateCellsDtosByMonthRecord(monthRecordDto.getCreated());
            ReflectionUtils.setFieldValue(monthRecordDto, "dateCells", dateCellDtos, true);
        }
    }

}

