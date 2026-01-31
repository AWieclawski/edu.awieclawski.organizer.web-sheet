package edu.springboot.organizer.web.controllers.forms;

import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.dtos.RecordsSetDto;
import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.facades.RecordsSetFacade;
import edu.springboot.organizer.web.wrappers.ResultsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * Knowledge sources:
 * <a href="https://www.baeldung.com/thymeleaf-variables">...</a>
 * <a href="https://www.concretepage.com/thymeleaf/thymeleaf-form-action">...</a>
 * <a href="https://www.baeldung.com/thymeleaf-list">...</a>
 * <a href="https://codingtechroom.com/question/bind-object-list-thymeleaf">...</a>
 * <a href="https://www.codejava.net/frameworks/spring-boot/spring-boot-thymeleaf-form-handling-tutorial">...</a>
 */

@Slf4j
@Controller
@RequestMapping("/${endpoint.form-date}")
@RequiredArgsConstructor
@DependsOn(RecordsSetFacade.BEAN_NAME)
public class MonthRecordsFormController {

    public final static String EDIT_ALL = "edit-all";
    public final static String EDIT_ONE = "edit-one";

    private final RecordsSetFacade monthRecordsFacade;

    @GetMapping(path = EDIT_ALL + "/{lookDate}")
    public ModelAndView getRecordsSetsForm(@PathVariable String lookDate,
                                           @ModelAttribute("flashAttribute") RecordsSetDto recordsSetDtoAttr) {
        Date date = monthRecordsFacade.getSearchDate(lookDate);
        List<RecordsSetDto> recordsSets = monthRecordsFacade.getRecordsDtoSets(date);
        RecordsSetDto recordsSetDto;
        ResultsDto resultsDto;
        ModelAndView mv = getViewPage("records-set-form");
        if (recordsSetDtoAttr != null && recordsSetDtoAttr.getMonth() != null) {
            recordsSetDto = recordsSetDtoAttr;
            mv.addObject("hasError", Boolean.TRUE);
        } else {
            recordsSetDto = recordsSets.stream().findFirst().orElseThrow(ResultNotFoundException::new);
            resultsDto = monthRecordsFacade.getValidatedResults(recordsSetDto);
            mv.addObject("hasError", resultsDto.getIsError());
        }
        mv.addObject("lookDate", lookDate);
        mv.addObject("monthName", monthRecordsFacade.getMonthName(recordsSetDto.getMonth()));
        mv.addObject("recordsSet", recordsSetDto);
        mv.addObject("viewRedirect", monthRecordsFacade.getViewRedirect());
        return mv;
    }

    @GetMapping(path = EDIT_ONE + "/{monthRecordId}")
    public ModelAndView getMonthDateForm(@PathVariable String monthRecordId,
                                         @ModelAttribute("flashAttribute") MonthRecordDto dtoAttribute) {
        MonthRecordDto monthRecordDto;
        ResultsDto resultsDto;
        ModelAndView mv = getViewPage("month-days-form");
        if (dtoAttribute == null || dtoAttribute.getCreated() == null) {
            monthRecordDto = monthRecordsFacade.findMonthRecordDtoById(monthRecordId);
            resultsDto = monthRecordsFacade.getValidatedResults(monthRecordDto);
            mv.addObject("hasError", resultsDto.getIsError());
            monthRecordDto = resultsDto.getMonthRecordDto();
        } else {
            monthRecordDto = dtoAttribute;
            mv.addObject("hasError", Boolean.TRUE);
        }
        if (monthRecordDto == null) {
            throw new ResultNotFoundException();
        }
        String lookDate = monthRecordsFacade.getLookDateFromMonthRecordById(monthRecordId);
        mv.addObject("lookDate", lookDate);
        mv.addObject("monthRecord", monthRecordDto);
        mv.addObject("viewRedirect", monthRecordsFacade.getViewRedirect());
        return mv;
    }

    private ModelAndView getViewPage(String viewPage) {
        return new ModelAndView(viewPage);
    }

}
