package edu.springboot.organizer.web.services;

import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.data.repositories.VisitorRepository;
import edu.springboot.organizer.web.dtos.VisitorDto;
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
@Service(value = VisitorService.BEAN_NAME)
@DependsOn(VisitorRepository.BEAN_NAME)
public class VisitorService extends BaseService<Visitor, VisitorDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.web.services.VisitorService";

    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        super();
        this.visitorRepository = visitorRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public VisitorDto createVisitor(Visitor visitor) {
        return createEntity(visitor);
    }

    @Transactional(readOnly = true)
    public List<VisitorDto> getAllVisitors() {
        return getAllEntities();
    }

    public List<VisitorDto> getVisitorsBetweenDates(String startDate, String endDate) {
        try {
            List<Visitor> entities = visitorRepository.findVisitorsByTimestampIsBetween(startDate, endDate);
            return entities.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Get between failed! {}", e.getMessage());
        }
        throw new ResultNotFoundException(String.format("Visitors search between %s %s failed!", startDate, endDate));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void purgeVisitors() {
        purgeEntities();
    }

    @Override
    public void initTable() {
        initTable(Visitor.getSqlTableCreator(), Visitor.TABLE_NAME);
    }

    @Override
    protected void setEntityClass() {
        this.dtoClass = VisitorDto.class;
    }

    @Override
    protected VisitorRepository getRepository() {
        return this.visitorRepository;
    }

    @Override
    protected BaseRowMapper<Visitor, VisitorDto> getRowMapper() {
        return getRepository().getBaseRowMapper();
    }
}

