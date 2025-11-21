package edu.springboot.organizer.web.services.base;


import edu.springboot.organizer.data.exceptions.PersistEntityException;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseService<S extends BaseEntity, T extends BaseDto> {

    protected Class<T> dtoClass;
    protected Class<S> entityClass;

    public BaseService() {
        setEntityClass();
    }

    protected T createEntity(S entity) {
        S saved = insertEntity(entity);
        if (log.isDebugEnabled()) {
            log.debug("Saved [{}|{}]", saved, saved.getClass().getSimpleName());
        }
        return getRowMapper().toDto(entity);
    }

    protected T createEntity(T dto) {
        S entity = getRowMapper().toEntity(dto);
        return createEntity(entity);
    }

    private S insertEntity(S entity) {
        try {
            return getRepository().persistEntity(entity);
        } catch (Exception e) {
            log.error("Persist Entity [{}] failed!", entity, e);
            throw new PersistEntityException("Persist error! " + (e.getMessage() != null ? e.getMessage() : e.getCause().getMessage()));
        }
    }

    protected List<T> persistDtos(List<T> dtos) {
        try {
            return getRepository().insertDtos(dtos);
        } catch (Exception e) {
            log.error("Persist {} dtos list failed! ", getRepository().getTableName(), e);
        }
        throw new ResultNotFoundException("Persist List error!");

    }

    protected List<T> updateDtos(List<T> dtos) {
        try {
            return getRepository().updateDtos(dtos);
        } catch (Exception e) {
            log.error("Update {} dtos list failed! ", getRepository().getTableName(), e);
        }
        throw new ResultNotFoundException("Update List error!");

    }

    protected List<T> getAllDtos() {
        try {
            List<S> entities = getRepository().findAll();
            return entities.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("All {} entities not found! ", getRepository().getTableName(), e);
        }
        throw new ResultNotFoundException("All entities found failed!");
    }

    protected List<T> getDtosByIds(List<String> ids) {
        try {
            List<S> entities = getRepository().findEntitiesByIds(ids);
            return entities.stream().map(getRowMapper()::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Find By Ids {} entities not found! ", getRepository().getTableName(), e);
        }
        throw new ResultNotFoundException("All entities found failed!");
    }

    protected T getEntityById(String id) {
        try {
            S entity = getRepository().findById(id);
            return getRowMapper().toDto(entity);
        } catch (Exception e) {
            log.error("{} entity [{}] not found! ", getRepository().getTableName(), id, e);
        }
        throw new ResultNotFoundException("Entity search by id failed!");
    }

    protected void purgeEntities() {
        String table = getRepository().getTableName();
        log.warn("Purge table [{}]", table);
        try {
            getRepository().deleteAll();
        } catch (Exception e) {
            log.error("Purge failed! {}", getRepository().getTableName(), e);
        }
    }

    protected void initTable(String sql, String table) {
        log.warn("Creating table [{}]", table);
        try {
            getRepository().modifyDataBase(sql);
        } catch (Exception e) {
            log.error("Modify failed! {}", getRepository().getTableName(), e);
        }
    }

    protected T entityUpdate(S entity) {
        Map<String, Object> parameters = getRowMapper().toMap(entity);
        try {
            S updated = getRepository().updateEntity(parameters, entity);
            if (log.isDebugEnabled()) {
                log.debug("Updated [{}|{}]", updated, updated.getClass().getSimpleName());
            }
        } catch (Exception e) {
            log.error("Update failed! {}", getRepository().getTableName(), e);
        }
        return getRowMapper().toDto(entity);
    }

    protected T dtoUpdate(T dto) {
        Map<String, Object> parameters = getRowMapper().toMap(dto);
        try {
            S updated = getRepository().updateDto(parameters, dto);
            if (log.isDebugEnabled()) {
                log.debug("Updated [{}|{}]", updated, updated.getClass().getSimpleName());
            }
        } catch (Exception e) {
            log.error("Update failed! {}", getRepository().getTableName(), e);
        }
        return dto;
    }

    protected Integer deleteEntity(String id) {
        try {
            return getRepository().deleteById(id);
        } catch (Exception e) {
            log.error("Delete failed [{}|{}]", id, getRepository().getTableName(), e);
        }
        return 0;
    }

    protected Integer deleteEntities(List<String> ids) {
        try {
            return getRepository().deleteByIdSet(new HashSet<>(ids));
        } catch (Exception e) {
            log.error("Delete failed [{}|{}]", ids, getRepository().getTableName());
        }
        return 0;
    }


    protected abstract BaseRepository<S, T> getRepository();

    protected abstract BaseRowMapper<S, T> getRowMapper();

    protected abstract void initTable();

    protected abstract void setEntityClass();

}
