package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.daos.DateCellDao;
import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.web.dtos.DateCellDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository(DateCellRepository.BEAN_NAME)
@DependsOn(DateCellDao.BEAN_NAME)
public class DateCellRepository extends BaseRepository<DateCell, DateCellDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.DateCellRepository";

    public DateCellRepository(DateCellDao dateCellDao
    ) {
        super(dateCellDao);
    }

    @Override
    public DateCell findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<DateCell> findAll() {
        return super.findAll();
    }

    public List<DateCell> findDateCellsByIds(List<String> ids) {
        return super.findEntitiesByIds(ids);
    }

    @Override
    public Long howMany() {
        return super.howMany();
    }

    public List<DateCell> findDateCellsByDate(String dateTime) {
        return ((DateCellDao) getBaseDao()).findDateCellsByDate(dateTime);
    }

    public List<DateCell> findDateCellsByMonthRecordId(String monthRecordId) {
        return ((DateCellDao) getBaseDao()).findDateCellsByMonthRecordId(monthRecordId);
    }

    public List<DateCell> findDateCellsByDateAndMonthRecordId(String dateTime, String monthRecordId) {
        return ((DateCellDao) getBaseDao()).findDateCellsByDateAndMonthRecordId(dateTime, monthRecordId);
    }

}
