package edu.springboot.organizer.web.services.base;


import edu.springboot.organizer.data.exceptions.PersistEntityException;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseService<S extends BaseEntity, T extends BaseDto> {

    protected Class<T> dtoClass;

    public BaseService() {
        setEntityClass();
    }

    protected T createEntity(S entity) {
        S saved = insertEntity(entity);
        log.info("Saved [{}|{}]", saved, saved.getClass().getSimpleName());
        return getRowMapper().toDto(entity);
    }

    protected S insertEntity(S entity) {
        try {
            return getRepository().persistEntity(entity);
        } catch (Exception e) {
            log.error("Persist Entity [{}] failed!", entity, e);
            throw new PersistEntityException("Error! " + (e.getMessage() != null ? e.getMessage() : e.getCause().getMessage()));
        }
    }

    public List<T> getAllEntities() {
        try {
            List<S> entities = getRepository().findAll();
            return entities.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("All {} entities not found! | {}", dtoClass.getSimpleName(), e.getMessage());
        }
        throw new ResultNotFoundException("All entities found failed!");
    }

    public T getEntityById(String id) {
        try {
            S entity = getRepository().findById(id);
            return getRowMapper().toDto(entity);
        } catch (Exception e) {
            log.error("{} entity [{}] not found! {}", dtoClass, id, e.getMessage());
        }
        throw new ResultNotFoundException("Credential search by id failed!");
    }

    protected void purgeEntities() {
        String table = getRepository().getTableName();
        log.warn("Purge table [{}]", table);
        try {
            getRepository().deleteAll();
        } catch (Exception e) {
            log.error("Purge failed! {}", e.getMessage());
        }
    }

    protected void initTable(String sql, String table) {
        log.warn("Crating table [{}]", table);
        try {
            getRepository().modifyDataBase(sql);
        } catch (Exception e) {
            log.error("Modify failed! {}", e.getMessage());
        }
    }


    protected abstract BaseRepository<S, T> getRepository();

    protected abstract BaseRowMapper<S, T> getRowMapper();

    protected abstract void initTable();

    protected abstract void setEntityClass();

}
