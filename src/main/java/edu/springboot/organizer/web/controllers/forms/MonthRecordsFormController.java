package edu.springboot.organizer.web.controllers.forms;

import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.dtos.RecordsSetDto;
import edu.springboot.organizer.web.exceptions.ControllerException;
import edu.springboot.organizer.web.exceptions.ResultNotFoundException;
import edu.springboot.organizer.web.facades.RecordsSetFacade;
import edu.springboot.organizer.web.wrappers.DateCellsForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

/**
 * Knowledge sources:
 * https://www.baeldung.com/thymeleaf-variables
 * https://www.concretepage.com/thymeleaf/thymeleaf-form-action
 * https://www.baeldung.com/thymeleaf-list
 * https://codingtechroom.com/question/bind-object-list-thymeleaf
 */

@Slf4j
@RestController
@RequestMapping("/${endpoint.form-date}")
@RequiredArgsConstructor
public class MonthRecordsFormController {

    @Value("${endpoint.view-date}")
    private String viewRedirect;

    private final RecordsSetFacade monthRecordsFacade;

    @GetMapping(path = "{id}")
    public ModelAndView monthDateForm(Model model, @PathVariable String id) {
        model.addAttribute("monthRecordId", id);
        RecordsSetDto recordsSetDto = monthRecordsFacade.findRecordsSets(id);
        if (recordsSetDto == null) {
            throw new ResultNotFoundException();
        }
        String monthName = monthRecordsFacade.getMonthName(recordsSetDto.getMonth());
        model.addAttribute("monthName", monthName);
        model.addAttribute("month", recordsSetDto.getMonth());
        model.addAttribute("year", recordsSetDto.getYear());
        model.addAttribute("monthRecords", recordsSetDto.getMonthRecordDtoList());
        model.addAttribute("recordsSet", recordsSetDto);
        model.addAttribute("viewRedirect", getViewRedirect());
        return new ModelAndView("month-days-form");
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


    private String getViewRedirect() {
        if (viewRedirect != null) {
            return viewRedirect;
        }
        throw new ControllerException("Path variable not found!");
    }

}
