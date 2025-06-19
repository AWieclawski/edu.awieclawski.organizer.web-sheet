package edu.springboot.organizer.generator.services.base;


import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.generator.dtos.base.BaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component(BaseService.BEAN_NAME)
public abstract class BaseService<S extends BaseEntity, T extends BaseDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.generator.services.base.BaseService";

    private static final int MAX_TRY_COUNT = 3;

    @Transactional()
    public T insertEntity(S visitor) {
        return insertEntityExecute(visitor, 0);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public T insertEntityExecute(S visitor, int count) {
        try {
            return getRepository().persistEntity(visitor);
        } catch (Exception e) {
            log.error("Save failed! {}", e.getMessage());
            if (count < MAX_TRY_COUNT) {
                insertEntityExecute(visitor, ++count);
            }
        }
        return null;
    }

    protected abstract BaseRepository<S, T> getRepository();

}
