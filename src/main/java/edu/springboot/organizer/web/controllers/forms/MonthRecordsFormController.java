package edu.springboot.organizer.web.controllers.forms;

import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.facades.RecordsSetFacade;
import edu.springboot.organizer.web.wrappers.MonthRecordMV;
import edu.springboot.organizer.web.wrappers.RecordsSetMV;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * Knowledge sources:
 * https://www.baeldung.com/thymeleaf-variables
 * https://www.concretepage.com/thymeleaf/thymeleaf-form-action
 * https://www.baeldung.com/thymeleaf-list
 * https://codingtechroom.com/question/bind-object-list-thymeleaf
 * https://www.codejava.net/frameworks/spring-boot/spring-boot-thymeleaf-form-handling-tutorial
 */

@Slf4j
@RestController
@RequestMapping("/${endpoint.form-date}")
@RequiredArgsConstructor
public class MonthRecordsFormController {

    private final RecordsSetFacade monthRecordsFacade;

    @GetMapping(path = "edit-all/{lookDate}")
    public ModelAndView getRecordsSetsForm(@PathVariable String lookDate) {
        Date date = monthRecordsFacade.getSearchDate(lookDate);
        List<RecordsSetMV> recordsSets = monthRecordsFacade.getRecordsSets(date);
        RecordsSetMV recordsSetMV = recordsSets.stream().findFirst().orElseThrow(ResultNotFoundException::new);
        if (recordsSetMV == null) {
            throw new ResultNotFoundException();
        }
        ModelAndView mv = getViewPage("records-set-form");
        mv.addObject("lookDate", lookDate);
        mv.addObject("monthName", monthRecordsFacade.getMonthName(recordsSetMV.getMonth()));
        mv.addObject("recordsSet", recordsSetMV);
        mv.addObject("viewRedirect", monthRecordsFacade.getViewRedirect());
        return mv;
    }

    @GetMapping(path = "edit-one/{monthRecordId}")
    public ModelAndView getMonthDateForm(@PathVariable String monthRecordId) {
        MonthRecordMV monthRecordMV = monthRecordsFacade.findMonthRecordById(monthRecordId);
        if (monthRecordMV == null) {
            throw new ResultNotFoundException();
        }
        String lookDate = monthRecordsFacade.getLookDateFromMonthRecordById(monthRecordId);
        ModelAndView mv = getViewPage("month-days-form");
        mv.addObject("lookDate", lookDate);
        mv.addObject("monthRecord", monthRecordMV);
        mv.addObject("viewRedirect", monthRecordsFacade.getViewRedirect());
        return mv;
    }

    private ModelAndView getViewPage(String viewPage) {
        return new ModelAndView(viewPage);
    }

}
