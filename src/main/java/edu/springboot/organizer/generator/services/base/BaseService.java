package edu.springboot.organizer.generator.services.base;


import edu.springboot.organizer.data.exceptions.PersistEntityException;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.generator.dtos.base.BaseDto;
import edu.springboot.organizer.generator.mappers.base.BaseRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseService<S extends BaseEntity, T extends BaseDto> {

    public S insertEntity(S entity) {
        try {
            return getRepository().persistEntity(entity);
        } catch (Exception e) {
            log.error("Persist Entity [{}] failed!", entity, e);
            throw new PersistEntityException("Error! " + (e.getMessage() != null ? e.getMessage() : e.getCause().getMessage()));
        }
    }

    public void purgeEntities(String table) {
        log.warn("Purge table [{}]", table);
        try {
            getRepository().deleteAll();
        } catch (Exception e) {
            log.error("Purge failed! {}", e.getMessage());
        }
    }

    public void initTable(String sql, String table) {
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

}
