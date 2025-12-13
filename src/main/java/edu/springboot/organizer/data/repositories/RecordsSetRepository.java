package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.daos.RecordsSetDao;
import edu.springboot.organizer.data.models.RecordsSet;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.web.dtos.RecordsSetDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository(RecordsSetRepository.BEAN_NAME)
@DependsOn(RecordsSetDao.BEAN_NAME)
public class RecordsSetRepository extends BaseRepository<RecordsSet, RecordsSetDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.RecordsSetRepository";

    public RecordsSetRepository(RecordsSetDao recordsSetDao) {
        super(recordsSetDao);
    }

    @Override
    public RecordsSet findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<RecordsSet> findAll() {
        return super.findAll();
    }

    @Override
    public Long howMany() {
        return super.howMany();
    }

    public List<RecordsSet> findRecordsSetByUser(String userId) {
        return ((RecordsSetDao) getBaseDao()).findRecordsSetByUser(userId);
    }

    public List<RecordsSet> findRecordsSetByMonthYearUser(int month, int year, String userId) {
        return ((RecordsSetDao) getBaseDao()).findRecordsSetByMonthYearUser(month, year, userId);
    }
}
