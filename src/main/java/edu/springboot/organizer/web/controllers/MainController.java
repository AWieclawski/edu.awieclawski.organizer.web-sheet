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

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping()
@RequiredArgsConstructor
public class MainController {

    @Value("${endpoint.pick-date}")
    private String viewRedirect;

    @GetMapping(path = {"", "/", "/home"})
    public ModelAndView veryStart(HttpServletRequest httpServletRequest) {
        log.info("Root endpoint [GET] received");
        String link = String.valueOf(httpServletRequest.getRequestURL());
        ModelAndView mv = getViewPage("welcome");
        mv.addObject("urLink", link);
        return mv;
    }

    @GetMapping("/pick-date")
    public ModelAndView pickDate(HttpServletRequest httpServletRequest) {
        ModelAndView mv = getRedirectViewPage();
        String link = String.valueOf(httpServletRequest.getRequestURL());
        mv.addObject("urLink", link);
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
