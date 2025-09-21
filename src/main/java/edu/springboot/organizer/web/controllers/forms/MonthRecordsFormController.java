package edu.springboot.organizer.web.controllers.forms;

import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.facades.MonthRecordsFacade;
import edu.springboot.organizer.web.wrappers.DateCellsForm;
import edu.springboot.organizer.web.wrappers.DatePickerForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    private final MonthRecordsFacade monthRecordsFacade;

    @PostMapping(value = "")
    public ModelAndView monthDateDisplay(Model model, @ModelAttribute("datePickerForm") DatePickerForm datePickerForm) {
        model.addAttribute("datePickerForm", datePickerForm);
        List<MonthRecordDto> monthRecordDtos = monthRecordsFacade.getMonthRecords(datePickerForm.getLookDate());
        String monthName = monthRecordsFacade.getMonthName(datePickerForm.getLookDate());
        model.addAttribute("monthName", monthName);
        model.addAttribute("monthRecords", monthRecordDtos);
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

}
