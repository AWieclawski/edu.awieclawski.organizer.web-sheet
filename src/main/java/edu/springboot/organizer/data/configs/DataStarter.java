package edu.springboot.organizer.data.configs;

import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.web.dtos.VisitorDto;
import edu.springboot.organizer.web.dtos.base.BaseDto;
import edu.springboot.organizer.web.services.CredentialService;
import edu.springboot.organizer.web.services.DateCellService;
import edu.springboot.organizer.web.services.EmployeeService;
import edu.springboot.organizer.web.services.MonthRecordService;
import edu.springboot.organizer.web.services.RecordsSetService;
import edu.springboot.organizer.web.services.UserService;
import edu.springboot.organizer.web.services.VisitorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component()
@DependsOn(value = VisitorService.BEAN_NAME)
@RequiredArgsConstructor
public class DataStarter {

    @Value("${populate.initial.data:false}")
    private String populateData;

    private final VisitorService visitorService;
    private final UserService userService;
    private final DateCellService dateCellService;
    private final MonthRecordService monthRecordService;
    private final RecordsSetService recordsSetService;
    private final EmployeeService employeeService;
    private final CredentialService credentialService;

    @PostConstruct
    public void displaySystemInfo() {
        try {
            initDataDefinition();
            if (Boolean.parseBoolean(populateData)) {
                populateDataTest();
            }
        } catch (Exception ex) {
            log.error("Init data failed! {} | {}", ex.getCause(), ex.getMessage());
        }
    }

    private void initDataDefinition() {
        visitorService.initTable();
        credentialService.initTable();
        userService.initTable();
        employeeService.initTable();
        dateCellService.initTable();
        monthRecordService.initTable();
        recordsSetService.initTable();
    }

    private void populateDataTest() {
        IntStream.range(0, 10)
                .forEach(it -> visitorService.createVisitor(Visitor.builder().name("TEST_I_" + it).ip("DEMO").build()));
        List<VisitorDto> visitorsByIP = visitorService.getVisitorsByIP("DEMO");
        log.info("VisitorService demo after create {}", visitorsByIP.size());
        List<VisitorDto> visitorsByIds = visitorService.getVisitorsByIds(visitorsByIP.stream().map(VisitorDto::getCreated).collect(Collectors.toList()));
        log.info("VisitorService demo by ids {}", visitorsByIds.size());
        visitorService.deleteVisitors(visitorsByIP.stream().map(BaseDto::getCreated).collect(Collectors.toList()));
        log.info("VisitorService demo after delete {}", visitorService.getVisitorsByIP("DEMO").size());
    }

}
