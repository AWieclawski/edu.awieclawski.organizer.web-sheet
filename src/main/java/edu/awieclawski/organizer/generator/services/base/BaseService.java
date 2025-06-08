package edu.awieclawski.organizer.generator.services.base;


import edu.awieclawski.organizer.data.models.base.BaseEntity;
import edu.awieclawski.organizer.data.repositories.BaseIdDateTimeFormater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component(BaseService.BEAN_NAME)
@DependsOn(BaseIdDateTimeFormater.BEAN_NAME)
public abstract class BaseService<T extends BaseEntity> {

    public static final String BEAN_NAME = "edu.awieclawski.organizer.generator.services.base.BaseService";

    private final BaseIdDateTimeFormater baseIdFormater;

    public void createVisitorId(T visitor) {
        try {
            String timeStampId = visitor.getId() != null ? visitor.getId() : LocalDateTime.now().format(getBaseFormatter().getFormatter());
            visitor.setId(timeStampId);
        } catch (Exception e) {
            log.error("Id create failed! {}", e.getMessage());
        }
    }

    public BaseIdDateTimeFormater getBaseFormatter() {
        return this.baseIdFormater;
    }

}
