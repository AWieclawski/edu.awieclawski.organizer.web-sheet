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

    private final DateCellRepository visitorRepository;

    public DateCellService(DateCellRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DateCellDto createVisitor(DateCell visitor) {
        return insertEntity(visitor);
    }

    @Transactional(readOnly = true)
    public List<DateCellDto> getAllVisitors() {
        try {
            return visitorRepository.findAll();
        } catch (Exception e) {
            log.error("All Visitors not found! {}", e.getMessage());
        }
        throw new ResultNotFoundException("All Visitors found failed!");
    }

    public List<DateCellDto> getVisitorsByDate(String dayDate) {
        try {
            return visitorRepository.findDateCellsByTimestamp(dayDate);
        } catch (Exception e) {
            log.error("Get between failed! {}", e.getMessage());
        }
        throw new ResultNotFoundException(String.format("DateCells find between %s failed!", dayDate));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeDateCells() {
        try {
            visitorRepository.deleteAll();
        } catch (Exception e) {
            log.error("Purge failed! {}", e.getMessage());
        }
    }

    public void initTable() {
        String sql = DateCell.getSqlTableCreator();
        log.warn("Crating table [{}]", DateCell.TABLE_NAME);
        try {
            visitorRepository.modifyDataBase(sql);
        } catch (Exception e) {
            log.error("Modify failed! {}", e.getMessage());
        }
    }

    @Override
    protected DateCellRepository getRepository() {
        return this.visitorRepository;
    }
}

