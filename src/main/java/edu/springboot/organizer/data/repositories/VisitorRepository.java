package edu.springboot.organizer.data.repositories;

import edu.springboot.organizer.data.daos.VisitorDao;
import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.web.dtos.VisitorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository(VisitorRepository.BEAN_NAME)
@DependsOn(VisitorDao.BEAN_NAME)
public class VisitorRepository extends BaseRepository<Visitor, VisitorDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.data.repositories.VisitorRepository";

    public VisitorRepository(VisitorDao visitorDao) {
        super(visitorDao);
    }

    public List<Visitor> findVisitorsByIds(List<String> ids) {
        return findEntitiesByIds(ids);
    }

    @Override
    public Visitor findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<Visitor> findAll() {
        return super.findAll();
    }

    @Override
    public Long howMany() {
        return super.howMany();
    }

    public List<Visitor> findVisitorsByTimestampIsBetween(String startDate, String endDate) {
        return ((VisitorDao) getBaseDao()).findVisitorsByTimestampIsBetween(startDate, endDate);
    }

    public List<Visitor> findVisitorsByIP(String ip) {
        return ((VisitorDao) getBaseDao()).findVisitorsByIP(ip);
    }

}
