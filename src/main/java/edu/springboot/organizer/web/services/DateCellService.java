package edu.springboot.organizer.web.services;

import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.repositories.DateCellRepository;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import edu.springboot.organizer.web.services.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service(value = DateCellService.BEAN_NAME)
@DependsOn(DateCellRepository.BEAN_NAME)
public class DateCellService extends BaseService<DateCell, DateCellDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.web.services.DateCellService";

    private final DateCellRepository dateCellRepository;

    public DateCellService(DateCellRepository dateCellRepository) {
        super();
        this.dateCellRepository = dateCellRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DateCellDto createDateCell(DateCell dateCell) {
        return createEntity(dateCell);
    }

    @Transactional(readOnly = true)
    public List<DateCellDto> getAllDateCells() {
        return getAllEntities();
    }

    @Transactional(readOnly = true)
    public DateCellDto getDateCellById(String id) {
        return getEntityById(id);
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

    public List<DateCellDto> createDateCells(List<DateCell> dateCells) {
        return dateCells.stream()
                .filter(Objects::nonNull)
                .map(this::createDateCell)
                .collect(Collectors.toList());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeDateCells() {
        purgeEntities();
    }

    @Override
    public void initTable() {
        initTable(DateCell.getSqlTableCreator(), DateCell.TABLE_NAME);
    }

    @Override
    protected void setEntityClass() {
        this.dtoClass = DateCellDto.class;
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

