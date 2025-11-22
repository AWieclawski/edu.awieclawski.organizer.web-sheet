package edu.springboot.organizer.web.controllers.views;

import edu.springboot.organizer.web.controllers.forms.MonthRecordsFormController;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.dtos.RecordsSetDto;
import edu.springboot.organizer.web.wrappers.ResultsDto;
import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.facades.RecordsSetFacade;
import edu.springboot.organizer.web.wrappers.DatePickerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public ModelAndView monthDateDisplay(@ModelAttribute("datePickerForm") DatePickerDto datePickerDto) {
        if (datePickerDto == null) {
            throw new ResultNotFoundException("Date form missed!");
        }
        monthRecordsFacade.getRecordsDtoSets(datePickerDto.getLookDate());
        String lookDate = monthRecordsFacade.getFromSearchDate(datePickerDto.getLookDate());
        return getRedirectViewPage(lookDate);
    }

    @PostMapping(value = "show/{lookDate}")
    public Object monthDateView(@PathVariable String lookDate,
                                      @ModelAttribute("recordsSet") RecordsSetDto recordsSet
            , RedirectAttributes redirectAttributes) {
        Date date = monthRecordsFacade.getSearchDate(lookDate);
        if (recordsSet == null) {
            throw new ResultNotFoundException("Record set missed!");
        }
        ResultsDto resultsDto = monthRecordsFacade.getValidatedResults(recordsSet);
        if (resultsDto.getIsError()) {
            redirectAttributes.addFlashAttribute("flashAttribute", resultsDto.getRecordsSetDto());
            String formRedirect = monthRecordsFacade.getFormRedirect();
            String redirectedUrl = formRedirect + "/" + MonthRecordsFormController.EDIT_ALL + "/" + lookDate;
            return "redirect:/" + redirectedUrl;
        }
        RecordsSetDto recordsSetDto = monthRecordsFacade.updateRecordsSet(date, recordsSet);
        return getViewModelPopulatedObjects("display-month-days", lookDate, recordsSetDto);
    }

    /**
     * https://codingtechroom.com/question/how-to-pass-parameters-when-redirecting-in-spring-mvc
     *
     * @return
     */
    @PostMapping(value = "record/{lookDate}")
    public Object monthDateProcess(@PathVariable String lookDate,
                                   @ModelAttribute("monthRecord") MonthRecordDto monthRecordDto
            , RedirectAttributes redirectAttributes) {
        if (monthRecordDto == null) {
            throw new ResultNotFoundException("Month Record missed!");
        }
        ResultsDto resultsDto = monthRecordsFacade.getValidatedResults(monthRecordDto);
        if (resultsDto.getIsError()) {
            redirectAttributes.addFlashAttribute("flashAttribute", resultsDto.getMonthRecordDto());
            String formRedirect = monthRecordsFacade.getFormRedirect();
            String redirectedUrl = formRedirect + "/" + MonthRecordsFormController.EDIT_ONE + "/" + monthRecordDto.getCreated();
            return "redirect:/" + redirectedUrl;
        }
        RecordsSetDto recordsSetDto = monthRecordsFacade.updateMonthRecord(monthRecordDto);
        return getViewModelPopulatedObjects("display-month-days", lookDate, recordsSetDto);
    }

    @GetMapping(path = "add/{lookDate}")
    public ModelAndView addMonthRecord(@PathVariable String lookDate) {
        Date date = monthRecordsFacade.getSearchDate(lookDate);
        List<RecordsSetDto> recordsSets = monthRecordsFacade.getRecordsDtoSets(date);
        RecordsSetDto recordsSetDto = recordsSets.stream().findFirst().orElseThrow(ResultNotFoundException::new);
        if (recordsSetDto == null) {
            throw new ResultNotFoundException("Record set missed!");
        }
        monthRecordsFacade.addNewMonthRecordDto(recordsSetDto);
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
        List<RecordsSetDto> recordsSets = monthRecordsFacade.getRecordsDtoSets(date);
        RecordsSetDto recordsSetDto = recordsSets.stream().findFirst().orElseThrow(ResultNotFoundException::new);
        if (recordsSetDto == null) {
            throw new ResultNotFoundException("Record set missed!");
        }
        return getViewModelPopulatedObjects("display-month-days", lookDate, recordsSetDto);
    }

    private ModelAndView getModelAndPage(String viewPage) {
        return new ModelAndView(viewPage);
    }

    private ModelAndView getRedirectViewPage(String lookDate) {
        String viewRedirect = monthRecordsFacade.getViewRedirect();
        return getModelAndPage("redirect:/" + viewRedirect + "/show/" + lookDate);
    }

    private ModelAndView getViewModelPopulatedObjects(String viewPage, String lookDate, RecordsSetDto recordsSetDto) {
        ModelAndView mv = getModelAndPage(viewPage);
        mv.addObject("lookDate", lookDate);
        mv.addObject("monthName", monthRecordsFacade.getMonthName(recordsSetDto.getMonth()));
        mv.addObject("recordsSet", recordsSetDto);
        mv.addObject("formRedirect", monthRecordsFacade.getFormRedirect());
        mv.addObject("viewRedirect", monthRecordsFacade.getViewRedirect());
        return mv;
    }

}
