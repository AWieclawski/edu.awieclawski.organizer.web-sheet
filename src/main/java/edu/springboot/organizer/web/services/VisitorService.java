package edu.springboot.organizer.web.services;

import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.data.repositories.VisitorRepository;
import edu.springboot.organizer.web.dtos.VisitorDto;
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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<VisitorDto> saveVisitors(List<VisitorDto> visitors) {
        return persistDtos(visitors);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<VisitorDto> updateVisitors(List<VisitorDto> visitors) {
        return updateDtos(visitors);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public VisitorDto updateVisitor(Visitor visitor) {
        return entityUpdate(visitor);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public VisitorDto updateVisitor(VisitorDto visitor) {
        Visitor visitorToUpd = getRowMapper().toEntity(visitor);
        return entityUpdate(visitorToUpd);
    }

    @Transactional(readOnly = true)
    public List<VisitorDto> getAllVisitors() {
        return getAllDtos();
    }

    @Transactional(readOnly = true)
    public List<VisitorDto> getVisitorsByIds(List<String> ids) {
        return getDtosByIds(ids);
    }

    @Transactional(readOnly = true)
    public List<VisitorDto> getVisitorsBetweenDates(String startDate, String endDate) {
        try {
            List<Visitor> entities = visitorRepository.findVisitorsByTimestampIsBetween(startDate, endDate);
            return entities.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Get between failed! ", e);
        }
        throw new ResultNotFoundException(String.format("Visitors search between %s %s failed!", startDate, endDate));
    }

    @Transactional(readOnly = true)
    public List<VisitorDto> getVisitorsByIP(String ip) {
        try {
            List<Visitor> entities = visitorRepository.findVisitorsByIP(ip);
            return entities.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Get by ip failed! ", e);
        }
        throw new ResultNotFoundException(String.format("Visitors search by ip %s failed!", ip));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Integer deleteVisitor(String id) {
        try {
            return deleteEntity(id);
        } catch (Exception e) {
            log.error("Visitor delete by id [{}] failed! | {}", id, e.getMessage(), e);
            throw new QueryException("Visitor delete  failed! " + id);
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteVisitors(List<String> ids) {
        try {
            int result = deleteEntities(ids);
            if (log.isDebugEnabled()) {
                log.debug("Deleted {} rows", result);
            }
        } catch (Exception e) {
            log.error("Visitors delete by ids failed! ", e);
            throw new QueryException("Visitors delete failed! ");
        }
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
        this.entityClass = Visitor.class;
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

