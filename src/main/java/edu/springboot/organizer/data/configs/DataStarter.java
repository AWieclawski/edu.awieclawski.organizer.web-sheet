package edu.springboot.organizer.data.configs;

import edu.springboot.organizer.data.models.DateCell;
import edu.springboot.organizer.data.models.Employee;
import edu.springboot.organizer.data.models.MonthRecord;
import edu.springboot.organizer.data.models.User;
import edu.springboot.organizer.data.models.Visitor;
import edu.springboot.organizer.generator.dtos.EmployeeDto;
import edu.springboot.organizer.generator.dtos.UserDto;
import edu.springboot.organizer.generator.enums.CellType;
import edu.springboot.organizer.generator.services.CredentialService;
import edu.springboot.organizer.generator.services.DateCellService;
import edu.springboot.organizer.generator.services.EmployeeService;
import edu.springboot.organizer.generator.services.MonthRecordService;
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
    private final DateCellService dateCellService;
    private final MonthRecordService monthRecordService;
    private final EmployeeService employeeService;
    private final CredentialService credentialService;

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
        credentialService.initTable();
        userService.initTable();
        employeeService.initTable();
        dateCellService.initTable();
        monthRecordService.initTable();
    }

    private void operateDemoData() {

        visitorService.purgeVisitors();
        visitorService.createVisitor(Visitor.builder().url("example/url/test0").name("test0").ip("ip0").build());
        visitorService.createVisitor(Visitor.builder().url("example/url/test1").name("test1").ip("ip1").timestamp(LocalDateTime.now().minusDays(1)).build());
        visitorService.createVisitor(Visitor.builder().url("example/url/test2").name("test2").ip("ip2").timestamp(LocalDateTime.now().minusDays(2)).build());
        visitorService.createVisitor(Visitor.builder().url("example/url/test3").name("test3").ip("ip3").timestamp(LocalDateTime.now().minusDays(3)).build());
        visitorService.createVisitor(Visitor.builder().url("example/url/test4").name("test4").ip("ip4").timestamp(LocalDateTime.now().minusDays(4)).build());
        visitorService.createVisitor(Visitor.builder().url("example/url/test5").name("test5").ip("ip5").timestamp(LocalDateTime.now().minusDays(5)).build());

        userService.purgeUsers();
        UserDto user1 = userService.createUser(User.builder().name("Examplename1").surName("Examplesurname1").credentialId("examplecredentials1").build());
        UserDto user2 = userService.createUser(User.builder().name("Examplename2").surName("Examplesurname2").credentialId("examplecredentials2").build());

        employeeService.purgeEmployees();
        EmployeeDto employee1 = employeeService.createEmployee(Employee.builder().id("employeeRecord01").surName("surname1").name("name1").uniqNick("nick01").build());
        EmployeeDto employee2 = employeeService.createEmployee(Employee.builder().id("employeeRecord02").surName("surname2").name("name2").uniqNick("nick02").build());

        dateCellService.purgeDateCells();
        dateCellService.createDateCell(DateCell.builder().monthRecordId("monthRecordId00").cellType(CellType.HOURS_RANGE).beginHour(8).endHour(16).localDate(LocalDateTime.now().minusDays(5).toLocalDate()).build());
        dateCellService.createDateCell(DateCell.builder().monthRecordId("monthRecordId00").cellType(CellType.HOURS_RANGE).beginHour(8).endHour(16).localDate(LocalDateTime.now().minusDays(4).toLocalDate()).build());
        dateCellService.createDateCell(DateCell.builder().monthRecordId("monthRecordId00").cellType(CellType.HOURS_RANGE).beginHour(8).endHour(16).localDate(LocalDateTime.now().minusDays(3).toLocalDate()).build());
        dateCellService.createDateCell(DateCell.builder().monthRecordId("monthRecordId00").cellType(CellType.HOURS_RANGE).beginHour(8).endHour(16).localDate(LocalDateTime.now().minusDays(2).toLocalDate()).build());
        dateCellService.createDateCell(DateCell.builder().monthRecordId("monthRecordId00").cellType(CellType.HOURS_RANGE).beginHour(8).endHour(16).localDate(LocalDateTime.now().minusDays(1).toLocalDate()).build());
        dateCellService.createDateCell(DateCell.builder().monthRecordId("monthRecordId01").cellType(CellType.HOURS_RANGE).beginHour(8).endHour(16).localDate(LocalDateTime.now().minusDays(5).toLocalDate()).build());
        dateCellService.createDateCell(DateCell.builder().monthRecordId("monthRecordId01").cellType(CellType.HOURS_RANGE).beginHour(8).endHour(16).localDate(LocalDateTime.now().minusDays(4).toLocalDate()).build());
        dateCellService.createDateCell(DateCell.builder().monthRecordId("monthRecordId01").cellType(CellType.HOURS_RANGE).beginHour(8).endHour(16).localDate(LocalDateTime.now().minusDays(3).toLocalDate()).build());
        dateCellService.createDateCell(DateCell.builder().monthRecordId("monthRecordId01").cellType(CellType.HOURS_RANGE).beginHour(8).endHour(16).localDate(LocalDateTime.now().minusDays(2).toLocalDate()).build());
        dateCellService.createDateCell(DateCell.builder().monthRecordId("monthRecordId01").cellType(CellType.HOURS_RANGE).beginHour(8).endHour(16).localDate(LocalDateTime.now().minusDays(1).toLocalDate()).build());

        monthRecordService.purgeMonthRecords();
        monthRecordService.createMonthRecord(MonthRecord.builder().id("monthRecordId00")
                .month(LocalDateTime.now().getMonthValue()).year(LocalDateTime.now().getYear())
                .userId(user1.getCreated()).employeeId(employee1.getCreated()).build());
        monthRecordService.createMonthRecord(MonthRecord.builder().id("monthRecordId01")
                .month(LocalDateTime.now().getMonthValue()).year(LocalDateTime.now().getYear())
                .userId(user2.getCreated()).employeeId(employee2.getCreated()).build());

    }
}
