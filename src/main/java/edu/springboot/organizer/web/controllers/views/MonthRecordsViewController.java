package edu.springboot.organizer.web.controllers.views;

import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.dtos.RecordsSetDto;
import edu.springboot.organizer.web.exceptions.ControllerException;
import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.facades.RecordsSetFacade;
import edu.springboot.organizer.web.wrappers.DateCellsForm;
import edu.springboot.organizer.web.wrappers.DatePickerForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

/**
 */

@Slf4j
@RestController
@RequestMapping("/${endpoint.view-date}")
@RequiredArgsConstructor
public class MonthRecordsViewController {

    @Value("${endpoint.form-date}")
    private String formRedirect;

    private final RecordsSetFacade monthRecordsFacade;

    @PostMapping(value = "")
    public ModelAndView monthDateDisplay(Model model, @ModelAttribute("datePickerForm") DatePickerForm datePickerForm) {
        model.addAttribute("datePickerForm", datePickerForm);
        List<RecordsSetDto> recordsSets = monthRecordsFacade.getRecordsSets(datePickerForm.getLookDate());
        RecordsSetDto recordsSetDto = recordsSets.stream().findFirst().orElseThrow(ResultNotFoundException::new);
        String monthName = monthRecordsFacade.getMonthName(datePickerForm.getLookDate());
        model.addAttribute("monthName", monthName);
        model.addAttribute("monthRecords", recordsSetDto.getMonthRecordDtoList());
        model.addAttribute("recordsSet", recordsSetDto);
        model.addAttribute("formRedirect", getFormRedirect());
        return new ModelAndView("display-date");
    }

    private void buildMonthRecordsForms(List<MonthRecordDto> monthRecordDtos, Model model) {
        monthRecordDtos.stream().filter(Objects::nonNull).forEach(mrDto ->
                {
                    DateCellsForm dcfWh = new DateCellsForm(mrDto.getCreated());
                    dcfWh.addDateCells(mrDto.getDateCellsList());
                    model.addAttribute(dcfWh.getDateCellFormId(), dcfWh.getDateCellsList());
                }
        );
    }


    private String getFormRedirect() {
        if (formRedirect != null) {
            return formRedirect;
        }
        throw new ControllerException("Path variable not found!");
    }

}
