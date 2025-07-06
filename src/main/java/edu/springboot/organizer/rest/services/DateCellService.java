package edu.springboot.organizer.rest.services;

import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.repositories.DateCellRepository;
import edu.springboot.organizer.rest.dtos.DateCellDto;
import edu.springboot.organizer.rest.exceptions.ResultNotFoundException;
import edu.springboot.organizer.rest.mappers.base.BaseRowMapper;
import edu.springboot.organizer.rest.services.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service(value = DateCellService.BEAN_NAME)
@DependsOn(DateCellRepository.BEAN_NAME)
public class DateCellService extends BaseService<DateCell, DateCellDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.rest.services.DateCellService";

    private final DateCellRepository dateCellRepository;

    public DateCellService(DateCellRepository dateCellRepository) {
        this.dateCellRepository = dateCellRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DateCellDto createDateCell(DateCell dateCell) {
        DateCell entity = insertEntity(dateCell);
        log.info("Saved [{}]", entity);
        return getRowMapper().toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<DateCellDto> getAllDateCells() {
        try {
            List<DateCell> dateCells = dateCellRepository.findAll();
            return dateCells.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("All DateCells not found! {}", e.getMessage());
        }
        throw new ResultNotFoundException("All DateCells found failed!");
    }

    @Transactional(readOnly = true)
    public DateCellDto getDateCellById(String id) {
        try {
            DateCell entity = dateCellRepository.findById(id);
            return getRowMapper().toDto(entity);
        } catch (Exception e) {
            log.error("DateCell [{}] not found! {}", id, e.getMessage());
        }
        throw new ResultNotFoundException("DateCell search by id failed!");
    }

    public List<DateCellDto> getDateCellsByDate(String dayDate) {
        try {
            List<DateCell> dateCells = dateCellRepository.findDateCellsByDate(dayDate);
            return dateCells.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Get DateCell by date failed! {}", e.getMessage());
        }
        throw new ResultNotFoundException(String.format("DateCell search by date %s failed!", dayDate));
    }

    public List<DateCellDto> getDateCellsByMonthRecord(String id) {
        try {
            List<DateCell> dateCells = dateCellRepository.findDateCellsByMonthRecordId(id);
            return dateCells.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Get by MonthRecord failed! {}", e.getMessage());
        }
        throw new ResultNotFoundException(String.format("DateCells find by MonthRecord %s failed!", id));
    }

    public List<DateCellDto> findDateCellsByDateAndMonthRecord(String dayDate, String id) {
        try {
            List<DateCell> dateCells = dateCellRepository.findDateCellsByDateAndMonthRecordId(dayDate, id);
            return dateCells.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Get by date and MonthRecord failed! {}", e.getMessage());
        }
        throw new ResultNotFoundException(String.format("DateCells find by MonthRecord %s failed!", id));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeDateCells() {
        purgeEntities(getRepository().getTableName());
    }

    @Override
    public void initTable() {
        initTable(DateCell.getSqlTableCreator(), DateCell.TABLE_NAME);
    }

    @Override
    protected DateCellRepository getRepository() {
        return this.dateCellRepository;
    }

    @Override
    protected BaseRowMapper<DateCell, DateCellDto> getRowMapper() {
        return getRepository().getBaseRowMapper();
    }
}

