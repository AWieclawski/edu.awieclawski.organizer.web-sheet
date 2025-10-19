package edu.springboot.organizer.web.controllers.views;

import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.facades.RecordsSetFacade;
import edu.springboot.organizer.web.wrappers.DatePickerForm;
import edu.springboot.organizer.web.wrappers.RecordsSetMV;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 *
 */

@Slf4j
@RestController
@RequestMapping("/${endpoint.view-date}")
@RequiredArgsConstructor
public class MonthRecordsViewController {

    private final RecordsSetFacade monthRecordsFacade;

    @PostMapping(value = "")
    public ModelAndView monthDateDisplay(@ModelAttribute("datePickerForm") DatePickerForm datePickerForm) {
        if (datePickerForm == null) {
            throw new ResultNotFoundException("Date form missed!");
        }
        List<RecordsSetMV> recordsSetDtoList = monthRecordsFacade.getRecordsSets(datePickerForm.getLookDate());
        RecordsSetMV recordsSetDto = recordsSetDtoList.stream().findFirst().orElseThrow(ResultNotFoundException::new);
        ModelAndView mv = getViewPage("display-month-days");
        mv.addObject("lookDate", monthRecordsFacade.getFromSearchDate(datePickerForm.getLookDate()));
        mv.addObject("monthName", monthRecordsFacade.getMonthName(datePickerForm.getLookDate()));
        mv.addObject("recordsSet", recordsSetDto);
        mv.addObject("formRedirect", monthRecordsFacade.getFormRedirect());
        return mv;
    }

    @PostMapping(value = "{lookDate}")
    public ModelAndView monthDateView(@PathVariable String lookDate,
                                      @ModelAttribute("recordsSet") RecordsSetMV recordsSet) {
        Date date = monthRecordsFacade.getSearchDate(lookDate);
        if (recordsSet == null) {
            throw new ResultNotFoundException("Record set missed!");
        }
        RecordsSetMV updatedRecordsSet = monthRecordsFacade.updateRecordsSet(date, recordsSet);
        ModelAndView mv = getViewPage("display-month-days");
        mv.addObject("lookDate", lookDate);
        mv.addObject("monthName", monthRecordsFacade.getMonthName(updatedRecordsSet.getMonth()));
        mv.addObject("recordsSet", updatedRecordsSet);
        mv.addObject("formRedirect", monthRecordsFacade.getFormRedirect());
        return mv;
    }

    @GetMapping(path = "{lookDate}")
    public ModelAndView getMonthDateForm(@PathVariable String lookDate) {
        Date date = monthRecordsFacade.getSearchDate(lookDate);
        List<RecordsSetMV> recordsSets = monthRecordsFacade.getRecordsSets(date);
        RecordsSetMV recordsSetDto = recordsSets.stream().findFirst().orElseThrow(ResultNotFoundException::new);
        if (recordsSetDto == null) {
            throw new ResultNotFoundException("Record set missed!");
        }
        ModelAndView mv = getViewPage("display-month-days");
        mv.addObject("lookDate", lookDate);
        mv.addObject("monthName", monthRecordsFacade.getMonthName(recordsSetDto.getMonth()));
        mv.addObject("recordsSet", recordsSetDto);
        mv.addObject("formRedirect", monthRecordsFacade.getFormRedirect());
        return mv;
    }

    private ModelAndView getViewPage(String viewPage) {
        return new ModelAndView(viewPage);
    }

}
