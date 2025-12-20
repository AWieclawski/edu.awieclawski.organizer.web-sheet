package edu.springboot.organizer.data.repositories.base;

import edu.springboot.organizer.data.daos.base.BaseEntityDao;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.mappers.base.BaseRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseRepository<S extends BaseEntity, T extends BaseDto> {

    private final BaseEntityDao<S, T> baseDao;

    public S findById(String id) {
        return getBaseDao().findById(id);
    }

    public List<S> findAll() {
        return getBaseDao().findAll();
    }

    public List<S> findEntitiesByIds(List<String> ids) {
        return getBaseDao().findEntitiesByIds(ids);
    }

    protected Long howMany() {
        return getBaseDao().howMany();
    }

    public Integer deleteByIdSet(HashSet<String> strings) {
        return getBaseDao().deleteByIdSet(strings);
    }

    public S persistEntity(S entity) {
        return getBaseDao().persistEntity(entity);
    }

    public List<T> insertDtos(List<T> dtos) throws InstantiationException {
        return getBaseDao().insertDtos(dtos);
    }

    public List<T> updateDtos(List<T> dtos) throws InstantiationException {
        return getBaseDao().updateDtos(dtos);
    }

    public void deleteAll() {
        getBaseDao().deleteAll();
    }

    public S updateEntity(Map<String, Object> parameters, S entity) throws InstantiationException {
        return getBaseDao().updateEntity(parameters, entity);
    }

    public String getTableName() {
        return getBaseDao().getTableName();
    }

    protected BaseEntityDao<S, T> getBaseDao() {
        if (baseDao != null) {
            return this.baseDao;
        }
        throw new BeanInitializationException("No BaseDao instance!");
    }

    public S updateDto(Map<String, Object> parameters, T dto) throws InstantiationException {
        return getBaseDao().updateDto(parameters, dto);
    }

    public Integer deleteById(String id) {
        return getBaseDao().deleteById(id);
    }

    public void modifyDataBase(String sql) {
        getBaseDao().modifyDataBase(sql);
    }

    public BaseRowMapper<S, T> getBaseRowMapper() {
        return getBaseDao().getBaseRowMapper();
    }
}
