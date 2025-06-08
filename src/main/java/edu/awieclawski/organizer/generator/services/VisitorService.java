package edu.awieclawski.organizer.generator.services;

import edu.awieclawski.organizer.generator.dtos.VisitorDto;
import edu.awieclawski.organizer.data.models.Visitor;
import edu.awieclawski.organizer.data.repositories.VisitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service(value = VisitorService.BEAN_NAME)
public class VisitorService {

    public static final String BEAN_NAME = "edu.albedo.data.services.VisitorService";

    private final VisitorRepository visitorRepository;

    public VisitorDto createVisitor(Visitor visitor) {
        LocalDateTime timeStamp = LocalDateTime.now();
        visitor.setTimestamp(timeStamp);
        return saveVisitor(visitor);
    }

    public VisitorDto saveVisitor(Visitor visitor) {
        LocalDateTime timeStamp = visitor.getTimestamp() != null ? visitor.getTimestamp() : LocalDateTime.now();
        visitor.setId(timeStamp.format(visitorRepository.getBaseIdFormater().getFormatter()));
        try {
            return visitorRepository.save(visitor);
        } catch (Exception e) {
            log.error("Save failed! {}", e.getMessage());
        }
        return null;
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

    public void modifyDataBase(String sql) {
        try {
            visitorRepository.modifyDataBase(sql);
        } catch (Exception e) {
            log.error("Modify failed! {}", e.getMessage());
        }
    }
}

