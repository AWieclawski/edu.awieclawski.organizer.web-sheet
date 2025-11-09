package edu.springboot.organizer.web.controllers;

import edu.springboot.organizer.web.controllers.forms.PickDateFormController;
import edu.springboot.organizer.web.exceptions.ControllerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping()
@RequiredArgsConstructor
public class MainController {

    @Value("${endpoint.pick-date}")
    private String viewRedirect;

    @GetMapping(path = {"","/", "/home"})
    public ModelAndView veryStart() {
        log.info("Root endpoint [GET] received");
        return new ModelAndView("welcome");
    }

    @GetMapping("/pick-date")
    public ModelAndView pickDate() {
        return getRedirectViewPage();
    }

    private ModelAndView getRedirectViewPage() {
        String viewRedirect = getViewRedirect();
        return getViewPage("redirect:/" + viewRedirect + PickDateFormController.CHOICE);
    }

    private ModelAndView getViewPage(String viewPage) {
        return new ModelAndView(viewPage);
    }

    private String getViewRedirect() {
        if (viewRedirect != null) {
            return viewRedirect;
        }
        throw new ControllerException("Path variable not found!");
    }

}
