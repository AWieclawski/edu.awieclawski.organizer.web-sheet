package edu.springboot.organizer.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.NestedServletException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class DataErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Throwable th = ((NestedServletException) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
        th = th != null ? th.getCause() : th;
        String causeMsg = th != null ? th.getMessage() : "";
        if (status != null) {
            model.addAttribute("errorMessage", String.format("%s %s", status, causeMsg != null ? ("- " + causeMsg) : ""));
        }
        return "errorpage";
    }
}
