package edu.springboot.organizer.generator.services.base;


import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.BaseDao;
import edu.springboot.organizer.generator.dtos.base.BaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component(BaseService.BEAN_NAME)
public abstract class BaseService<S extends BaseEntity, T extends BaseDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.generator.services.base.BaseService";

    public T insertEntity(S entity) {
        return getRepository().persistEntity(entity);
    }

    protected abstract BaseDao<S, T> getRepository();

}
