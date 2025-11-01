package edu.springboot.organizer.web.controllers.views;

import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.facades.RecordsSetFacade;
import edu.springboot.organizer.web.wrappers.DatePickerForm;
import edu.springboot.organizer.web.wrappers.MonthRecordMV;
import edu.springboot.organizer.web.wrappers.RecordsSetMV;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 *
 */

@Slf4j
@Controller
@RequestMapping("/${endpoint.view-date}")
@RequiredArgsConstructor
public class MonthRecordsViewController {

    private final RecordsSetFacade monthRecordsFacade;

    @PostMapping(value = "")
    public ModelAndView monthDateDisplay(@ModelAttribute("datePickerForm") DatePickerForm datePickerForm) {
        if (datePickerForm == null) {
            throw new ResultNotFoundException("Date form missed!");
        }
        monthRecordsFacade.getRecordsSets(datePickerForm.getLookDate());
        String lookDate = monthRecordsFacade.getFromSearchDate(datePickerForm.getLookDate());
        return getRedirectViewPage(lookDate);
    }

    @PostMapping(value = "show/{lookDate}")
    public ModelAndView monthDateView(@PathVariable String lookDate,
                                      @ModelAttribute("recordsSet") RecordsSetMV recordsSet) {
        Date date = monthRecordsFacade.getSearchDate(lookDate);
        if (recordsSet == null) {
            throw new ResultNotFoundException("Record set missed!");
        }
        RecordsSetMV recordsSetMV = monthRecordsFacade.updateRecordsSet(date, recordsSet);
        return getViewModelPopulatedObjects("display-month-days", lookDate, recordsSetMV);
    }

    @PostMapping(value = "record/{lookDate}")
    public ModelAndView monthDateProcess(@PathVariable String lookDate,
                                         @ModelAttribute("monthRecord") MonthRecordMV monthRecordMV) {
        if (monthRecordMV == null) {
            throw new ResultNotFoundException("Month Record missed!");
        }
        RecordsSetMV recordsSetMV = monthRecordsFacade.updateMonthRecord(monthRecordMV);
        return getViewModelPopulatedObjects("display-month-days", lookDate, recordsSetMV);
    }

    @GetMapping(path = "add/{lookDate}")
    public ModelAndView addMonthRecord(@PathVariable String lookDate) {
        Date date = monthRecordsFacade.getSearchDate(lookDate);
        List<RecordsSetMV> recordsSets = monthRecordsFacade.getRecordsSets(date);
        RecordsSetMV recordsSetMV = recordsSets.stream().findFirst().orElseThrow(ResultNotFoundException::new);
        if (recordsSetMV == null) {
            throw new ResultNotFoundException("Record set missed!");
        }
        monthRecordsFacade.addNewMonthRecordDto(recordsSetMV);
        return getRedirectViewPage(lookDate);
    }

    @GetMapping(path = "delete/{monthRecordId}")
    public ModelAndView deleteMonthRecord(@PathVariable String monthRecordId) {
        if (monthRecordId == null) {
            throw new ResultNotFoundException("Month Record Id missed!");
        }
        String lookDate = monthRecordsFacade.getLookDateFromMonthRecordById(monthRecordId);
        monthRecordsFacade.deleteMonthRecordById(monthRecordId);
        return getRedirectViewPage(lookDate);
    }

    @GetMapping(path = "show/{lookDate}")
    public ModelAndView getMonthDateForm(@PathVariable String lookDate) {
        Date date = monthRecordsFacade.getSearchDate(lookDate);
        List<RecordsSetMV> recordsSets = monthRecordsFacade.getRecordsSets(date);
        RecordsSetMV recordsSetMV = recordsSets.stream().findFirst().orElseThrow(ResultNotFoundException::new);
        if (recordsSetMV == null) {
            throw new ResultNotFoundException("Record set missed!");
        }
        return getViewModelPopulatedObjects("display-month-days", lookDate, recordsSetMV);
    }

    private ModelAndView getViewPage(String viewPage) {
        return new ModelAndView(viewPage);
    }

    private ModelAndView getRedirectViewPage(String lookDate) {
        String viewRedirect = monthRecordsFacade.getViewRedirect();
        return getViewPage("redirect:/" + viewRedirect + "/show/" + lookDate);
    }

    private ModelAndView getViewModelPopulatedObjects(String viewPage, String lookDate, RecordsSetMV recordsSetMV) {
        ModelAndView mv = getViewPage(viewPage);
        mv.addObject("lookDate", lookDate);
        mv.addObject("monthName", monthRecordsFacade.getMonthName(recordsSetMV.getMonth()));
        mv.addObject("recordsSet", recordsSetMV);
        mv.addObject("formRedirect", monthRecordsFacade.getFormRedirect());
        mv.addObject("viewRedirect", monthRecordsFacade.getViewRedirect());
        return mv;
    }

}
