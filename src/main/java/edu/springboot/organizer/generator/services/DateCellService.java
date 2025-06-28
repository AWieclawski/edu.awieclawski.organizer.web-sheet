package edu.springboot.organizer.generator.services;

import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.repositories.DateCellRepository;
import edu.springboot.organizer.generator.dtos.DateCellDto;
import edu.springboot.organizer.generator.exceptions.ResultNotFoundException;
import edu.springboot.organizer.generator.services.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service(value = DateCellService.BEAN_NAME)
@DependsOn(DateCellRepository.BEAN_NAME)
public class DateCellService extends BaseService<DateCell, DateCellDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.generator.services.DateCellService";

    private final DateCellRepository dateCellRepository;

    public DateCellService(DateCellRepository dateCellRepository) {
        this.dateCellRepository = dateCellRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DateCellDto createDateCell(DateCell dateCell) {
        return insertEntity(dateCell);
    }

    @Transactional(readOnly = true)
    public List<DateCellDto> getAllDateCells() {
        try {
            return dateCellRepository.findAll();
        } catch (Exception e) {
            log.error("All DateCells not found! {}", e.getMessage());
        }
        throw new ResultNotFoundException("All DateCells found failed!");
    }

    public List<DateCellDto> getDateCellsByDate(String dayDate) {
        try {
            return dateCellRepository.findDateCellsByTimestamp(dayDate);
        } catch (Exception e) {
            log.error("Get by date failed! {}", e.getMessage());
        }
        throw new ResultNotFoundException(String.format("DateCells find by date %s failed!", dayDate));
    }

    public List<DateCellDto> getDateCellsByMonthRecord(String id) {
        try {
            return dateCellRepository.findDateCellsByMonthRecordId(id);
        } catch (Exception e) {
            log.error("Get by MonthRecord failed! {}", e.getMessage());
        }
        throw new ResultNotFoundException(String.format("DateCells find by MonthRecord %s failed!", id));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeDateCells() {
        try {
            dateCellRepository.deleteAll();
        } catch (Exception e) {
            log.error("Purge failed! {}", e.getMessage());
        }
    }

    public void initTable() {
        String sql = DateCell.getSqlTableCreator();
        log.warn("Crating table [{}]", DateCell.TABLE_NAME);
        try {
            dateCellRepository.modifyDataBase(sql);
        } catch (Exception e) {
            log.error("Modify failed! {}", e.getMessage());
        }
    }

    @Override
    protected DateCellRepository getRepository() {
        return this.dateCellRepository;
    }
}

