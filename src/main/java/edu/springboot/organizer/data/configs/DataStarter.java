package edu.springboot.organizer.data.configs;

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
        } catch (Exception ex) {
            log.error("Demo data failed! {} | {}", ex.getCause(), ex.getMessage());
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

}
