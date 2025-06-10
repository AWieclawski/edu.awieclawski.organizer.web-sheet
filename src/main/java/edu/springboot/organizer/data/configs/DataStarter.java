package edu.springboot.organizer.data.configs;

import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.data.models.base.BaseEntity;
import edu.springboot.organizer.generator.services.VisitorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Slf4j
@Component()
@DependsOn(value = VisitorService.BEAN_NAME)
@RequiredArgsConstructor
public class DataStarter {

    @Value("${spring.profiles.active:test}")
    private String activeProfile;

    private final VisitorService service;

    @PostConstruct
    public void displaySystemInfo() {
        try {
            initDataDefinition();
            if (activeProfile.equals("dev")
                    || activeProfile.equals("test")
                    || activeProfile.equals("prod")
            ) {
                operateDemoData();
            }
        } catch (Exception ex) {
            log.error("Demo data failed! {} | {}", ex.getCause(), ex.getMessage());
        }

    }

    private void initDataDefinition() {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s ( %s TEXT PRIMARY KEY, %s TEXT, %s DATETIME DEFAULT CURRENT_TIMESTAMP, %s TEXT, %s TEXT);",
                Visitor.TABLE_NAME,
                BaseEntity.BaseConst.ID.getColumn(),
                Visitor.Const.NAME.getColumn(),
                Visitor.Const.TIMESTAMP.getColumn(),
                Visitor.Const.URL.getColumn(),
                Visitor.Const.IP.getColumn());
        service.modifyDataBase(sql);
    }

    private void operateDemoData() {

        service.purgeVisitors();

        // save a couple of demo visitors
        service.createVisitor(Visitor.builder().url("example/url/test0").name("test0").ip("ip0").build());
        service.saveVisitor(Visitor.builder().url("example/url/test1").name("test1").ip("ip1").timestamp(LocalDateTime.now().minusDays(1)).build());
        service.saveVisitor(Visitor.builder().url("example/url/test2").name("test2").ip("ip2").timestamp(LocalDateTime.now().minusDays(2)).build());
        service.saveVisitor(Visitor.builder().url("example/url/test3").name("test3").ip("ip3").timestamp(LocalDateTime.now().minusDays(3)).build());
        service.saveVisitor(Visitor.builder().url("example/url/test4").name("test4").ip("ip4").timestamp(LocalDateTime.now().minusDays(4)).build());
        service.saveVisitor(Visitor.builder().url("example/url/test5").name("test5").ip("ip5").timestamp(LocalDateTime.now().minusDays(5)).build());
    }
}
