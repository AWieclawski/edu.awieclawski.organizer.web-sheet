package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.daos.MonthRecordDao;
import edu.springboot.organizer.data.models.MonthRecord;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository(MonthRecordRepository.BEAN_NAME)
@DependsOn(MonthRecordDao.BEAN_NAME)
public class MonthRecordRepository extends BaseRepository<MonthRecord, MonthRecordDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.MonthRecordRepository";

    public MonthRecordRepository(MonthRecordDao monthRecordDao) {
        super(monthRecordDao);
    }

    @Override
    public MonthRecord findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<MonthRecord> findAll() {
        return super.findAll();
    }

    @Override
    public Long howMany() {
        return super.howMany();
    }

    public List<MonthRecord> findMonthRecordBySet(String setId) {
        return ((MonthRecordDao) getBaseDao()).findMonthRecordBySet(setId);
    }

}
