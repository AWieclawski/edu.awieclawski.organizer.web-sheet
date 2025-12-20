package edu.springboot.organizer.data.configs;

import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.utils.DateUtils;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
        long start;
        long finish;

        // create
        start = System.nanoTime();
        final String ipDemo = "DEMO_IP";
        IntStream.range(0, 10)
                .forEach(it -> visitorService.createVisitor(Visitor.builder().name("TEST_NAME_" + it).ip(ipDemo).build()));
        List<VisitorDto> visitorsByIP = visitorService.getVisitorsByIP(ipDemo);
        finish = System.nanoTime();
        log.info("VisitorService demo after separately created {} - time elapsed {}", visitorsByIP.size(), finish - start);
        List<VisitorDto> visitorsByIds = new ArrayList<>();
        try {
            visitorsByIds.addAll(visitorService.getVisitorsByIds(visitorsByIP.stream().map(VisitorDto::getCreated).collect(Collectors.toList())));
        } catch (Exception e) {
            log.warn("Error: {}", e.getMessage(), e);
        }
        log.info("VisitorService demo found by ids {}", visitorsByIds.size());

        // updateVisitors separately
        int[] intArr = new int[1];
        visitorsByIds.forEach(it -> it.setUrl("DEMO_URL_" + ++intArr[0]));
        final List<VisitorDto> visitorsByIdsUpdated = new ArrayList<>();
        start = System.nanoTime();
        try {
            visitorsByIds.forEach(it -> visitorsByIdsUpdated.add(visitorService.updateVisitor(it)));
        } catch (Exception e) {
            log.warn("Error: {}", e.getMessage(), e);
        }
        finish = System.nanoTime();
        log.info("VisitorService demo separately updated {} - time elapsed {}", visitorsByIdsUpdated.size(), finish - start);

        // updateVisitors list
        List<VisitorDto> visitorsByIdsUpdatedList = new ArrayList<>(visitorsByIdsUpdated);
        visitorsByIdsUpdatedList.forEach(it -> it.setTimestamp(DateUtils.getStringFromTimestamp(new Timestamp(new Date().getTime()), DateUtils.DATE_PATTERN_STANDARD)));
        start = System.nanoTime();
        try {
            visitorsByIdsUpdatedList = visitorService.updateVisitors(visitorsByIdsUpdatedList);
        } catch (Exception e) {
            log.warn("Error: {}", e.getMessage(), e);
        }
        finish = System.nanoTime();
        log.info("VisitorService demo list updated {} - time elapsed {}", visitorsByIdsUpdatedList.size(), finish - start);

        // save list
        visitorsByIdsUpdated.forEach(it -> it.setCreated(null));
        start = System.nanoTime();
        List<VisitorDto> visitorsSaved = new ArrayList<>();
        try {
            visitorsSaved.addAll(visitorService.saveVisitors(visitorsByIdsUpdated));
        } catch (Exception e) {
            log.warn("Error: {}", e.getMessage(), e);
        }
        finish = System.nanoTime();
        log.info("VisitorService demo list saved {} - time elapsed {}", visitorsSaved.size(), finish - start);

        // delete demo
        List<VisitorDto> visitorsByIPDemo = visitorService.getVisitorsByIP(ipDemo);
        try {
            visitorService.deleteVisitors(visitorsByIPDemo.stream().map(BaseDto::getCreated).collect(Collectors.toList()));
        } catch (Exception e) {
            log.warn("Error: {}", e.getMessage(), e);
        }
        log.info("VisitorService demo after delete {}", visitorService.getVisitorsByIP(ipDemo).size());
    }

}
