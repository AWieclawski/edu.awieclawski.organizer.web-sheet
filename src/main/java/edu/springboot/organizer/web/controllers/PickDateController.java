package edu.springboot.organizer.web.controllers;

import edu.springboot.organizer.web.exceptions.ControllerException;
import edu.springboot.organizer.web.wrappers.DatePicker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/${endpoint.pick-date}")
@RequiredArgsConstructor
public class PickDateController {

    @Value("${endpoint.form-date}")
    private String pathRedirect;

    @GetMapping("/choose-date")
    public ModelAndView chooseDateForm(Model model) {
        model.addAttribute("datePicker", new DatePicker());
        model.addAttribute("pathRedirect", getPathRedirect());
        return new ModelAndView("pick-date");
    }

    private String getPathRedirect() {
        if (pathRedirect != null) {
            return pathRedirect;
        }
        throw new ControllerException("Path variable not found!");
    }

}
