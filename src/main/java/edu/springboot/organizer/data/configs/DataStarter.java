package edu.springboot.organizer.data.configs;

import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.generator.services.UserService;
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

    @Value("${populate.initial.data:false}")
    private String populateData;

    private final VisitorService visitorService;
    private final UserService userService;

    @PostConstruct
    public void displaySystemInfo() {
        try {
            initDataDefinition();
            if (Boolean.TRUE.equals(Boolean.valueOf(populateData))) {
                log.info("start demo data population");
                operateDemoData();
                log.info("demo data population finished");
            }
        } catch (Exception ex) {
            log.error("Demo data failed! {} | {}", ex.getCause(), ex.getMessage());
        }

    }

    private void initDataDefinition() {
        visitorService.initTable();
        userService.initTable();
    }

    private void operateDemoData() {

        visitorService.purgeVisitors();

        // save a couple of demo visitors
        visitorService.createVisitor(Visitor.builder().url("example/url/test0").name("test0").ip("ip0").build());
        visitorService.createVisitor(Visitor.builder().url("example/url/test1").name("test1").ip("ip1").timestamp(LocalDateTime.now().minusDays(1)).build());
        visitorService.createVisitor(Visitor.builder().url("example/url/test2").name("test2").ip("ip2").timestamp(LocalDateTime.now().minusDays(2)).build());
        visitorService.createVisitor(Visitor.builder().url("example/url/test3").name("test3").ip("ip3").timestamp(LocalDateTime.now().minusDays(3)).build());
        visitorService.createVisitor(Visitor.builder().url("example/url/test4").name("test4").ip("ip4").timestamp(LocalDateTime.now().minusDays(4)).build());
        visitorService.createVisitor(Visitor.builder().url("example/url/test5").name("test5").ip("ip5").timestamp(LocalDateTime.now().minusDays(5)).build());

        userService.purgeUsers();

        // save a couple of demo users
        userService.createUser(User.builder().name("Examplename1").surName("Examplesurname1").login("examplelogin1").password("somepass1").build());
        userService.createUser(User.builder().name("Examplename2").surName("Examplesurname2").login("examplelogin2").password("somepass2").build());

    }
}
