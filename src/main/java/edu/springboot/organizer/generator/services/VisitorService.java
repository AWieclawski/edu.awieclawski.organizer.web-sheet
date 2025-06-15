package edu.springboot.organizer.generator.services;

import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.data.repositories.VisitorRepository;
import edu.springboot.organizer.generator.dtos.VisitorDto;
import edu.springboot.organizer.generator.services.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service(value = VisitorService.BEAN_NAME)
@DependsOn(VisitorRepository.BEAN_NAME)
public class VisitorService extends BaseService<Visitor, VisitorDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.generator.services.VisitorService";

    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public VisitorDto createVisitor(Visitor visitor) {
        LocalDateTime timeStamp = LocalDateTime.now();
        visitor.setTimestamp(timeStamp);
        return insertVisitor(visitor);
    }

    public List<VisitorDto> getAllVisitors() {
        return visitorRepository.findAll();
    }

    public List<VisitorDto> getVisitorsBetween(String startDate, String endDate) {
        try {
            return visitorRepository.findVisitorsByTimestampIsBetween(startDate, endDate);
        } catch (Exception e) {
            log.error("Get between failed! {}", e.getMessage());
        }
        return new ArrayList<>();
    }

    public void purgeVisitors() {
        try {
            visitorRepository.deleteAll();
        } catch (Exception e) {
            log.error("Purge failed! {}", e.getMessage());
        }
    }

    public void initTable() {
        String sql = Visitor.getSqlTableCreator();
        log.warn("Crating table [{}]", Visitor.TABLE_NAME);
        try {
            visitorRepository.modifyDataBase(sql);
        } catch (Exception e) {
            log.error("Modify failed! {}", e.getMessage());
        }
    }

    @Override
    protected VisitorRepository getRepository() {
        return this.visitorRepository;
    }
}

