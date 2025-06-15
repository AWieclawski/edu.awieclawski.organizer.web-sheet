package edu.springboot.organizer.generator.services.base;


import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.data.repositories.base.BaseRepository;
import edu.springboot.organizer.generator.dtos.base.BaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component(BaseService.BEAN_NAME)
public abstract class BaseService<S extends BaseEntity, T extends BaseDto> {

    public static final String BEAN_NAME = "edu.springboot.organizer.generator.services.base.BaseService";

    private static final int MAX_TRY_COUNT = 3;

    protected T insertVisitor(S visitor) {
        return insertVisitorExecute(visitor, 0);
    }

    private T insertVisitorExecute(S visitor, int count) {
        try {
            return getRepository().insert(visitor);
        } catch (Exception e) {
            log.error("Save failed! {}", e.getMessage());
            if (count < MAX_TRY_COUNT) {
                insertVisitorExecute(visitor, ++count);
            }
        }
        return null;
    }

    protected abstract BaseRepository<S, T> getRepository();

}
