package edu.springboot.organizer.web.services;

import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.repositories.DateCellRepository;
import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.exceptions.QueryException;
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
    public DateCellDto createDateCell(DateCellDto dateCellDto) {
        return createEntity(dateCellDto);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<DateCellDto> saveDateCells(List<DateCellDto> dateCellDtos) {
        return persistDtos(dateCellDtos);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DateCellDto updateDateCell(DateCellDto dateCell) {
        return dtoUpdate(dateCell);
    }

    @Transactional(readOnly = true)
    public List<DateCellDto> getAllDateCells() {
        return getAllDtos();
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

    public List<DateCellDto> getDateCellsDtosByMonthRecord(String id) {
        try {
            List<DateCell> dateCells = dateCellRepository.findDateCellsByMonthRecordId(id);
            return dateCells.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Get by MonthRecord failed! {}", e.getMessage());
        }
        throw new ResultNotFoundException(String.format("DateCells find by MonthRecord %s failed!", id));
    }

    public List<DateCell> getDateCellsByMonthRecord(String id) {
        try {
            return dateCellRepository.findDateCellsByMonthRecordId(id);
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
    public Integer deleteDateCell(String id) {
        try {
            return deleteEntity(id);
        } catch (Exception e) {
            log.error("DateCell delete by id [{}] failed! | {}", id, e.getMessage());
        }
        throw new QueryException("DateCell delete failed! " + id);
    }

    public List<DateCellDto> createDateCells(List<DateCellDto> dateCellDtos) {
        return saveDateCells(dateCellDtos);
    }

    public List<DateCellDto> updateDateCells(List<DateCellDto> dateCellDtos) {
        return updateDtos(dateCellDtos);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteDateCells(List<String> ids) {
        try {
            int result = deleteEntities(ids);
            if (log.isDebugEnabled()) {
                log.debug("Deleted {} rows", result);
            }
        } catch (Exception e) {
            log.error("DateCells delete by ids failed!", e);
            throw new QueryException("DateCells delete failed! ");
        }
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteDateCellsByMonthRecordId(String monthRecordId) {
        if (monthRecordId != null) {
            List<String> ids = getDateCellsDtosByMonthRecord(monthRecordId)
                    .stream().map(BaseDto::getCreated).collect(Collectors.toList());
            deleteDateCells(ids);
        }
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
        this.entityClass = DateCell.class;
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

