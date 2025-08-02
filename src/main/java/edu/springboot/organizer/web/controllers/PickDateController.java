package edu.springboot.organizer.web.controllers;

import edu.springboot.organizer.web.dtos.DatePickerDto;
import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.facades.MonthRecordsFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RestController
@RequestMapping("/${endpoint.pick-date}")
@RequiredArgsConstructor
public class PickDateController {

    private final MonthRecordsFacade monthRecordsFacade;

    @GetMapping("/choose-date")
    public ModelAndView chooseDateForm(Model model) {
        model.addAttribute("datePicker", new DatePickerDto());
        return new ModelAndView("pick-date");
    }

    @PostMapping(value = "/choose-date")
    public ModelAndView chooseDateDisplay(Model model, @ModelAttribute("datePicker") DatePickerDto datePicker) {
        model.addAttribute("datePicker", datePicker);
        List<MonthRecordDto> monthRecordDtos = monthRecordsFacade.populateMonthRecords(datePicker.getLookDate());
        model.addAttribute("monthRecords", monthRecordDtos);
        return new ModelAndView("display-date");
    }

}
