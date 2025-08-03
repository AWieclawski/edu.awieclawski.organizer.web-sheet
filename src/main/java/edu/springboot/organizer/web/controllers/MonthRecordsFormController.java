package edu.springboot.organizer.web.controllers;

import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.facades.MonthRecordsFacade;
import edu.springboot.organizer.web.wrappers.DatePicker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
    public ModelAndView monthDateDisplay(Model model, @ModelAttribute("datePicker") DatePicker datePicker) {
        model.addAttribute("datePicker", datePicker);
        List<MonthRecordDto> monthRecordDtos = monthRecordsFacade.populateMonthRecords(datePicker.getLookDate());
        model.addAttribute("monthRecords", monthRecordDtos);
        return new ModelAndView("display-date");
    }

}
