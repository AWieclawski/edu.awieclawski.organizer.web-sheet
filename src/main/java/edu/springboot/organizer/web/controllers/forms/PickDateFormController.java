package edu.springboot.organizer.web.controllers.forms;

import edu.springboot.organizer.web.exceptions.ControllerException;
import edu.springboot.organizer.web.wrappers.DatePickerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/${endpoint.pick-date}")
@RequiredArgsConstructor
public class PickDateFormController {

    public final static String CHOICE = "/choose-date";

    @Value("${endpoint.view-date}")
    private String viewRedirect;

    @GetMapping(CHOICE)
    public ModelAndView chooseDateForm(Model model) {
        model.addAttribute("datePickerForm", new DatePickerDto());
        model.addAttribute("viewRedirect", getViewRedirect());
        return new ModelAndView("pick-date");
    }

    private String getViewRedirect() {
        if (viewRedirect != null) {
            return viewRedirect;
        }
        throw new ControllerException("Path variable not found!");
    }

}
