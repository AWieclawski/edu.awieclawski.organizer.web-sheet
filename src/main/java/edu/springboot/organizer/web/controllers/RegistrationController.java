package edu.springboot.organizer.web.controllers;

import edu.springboot.organizer.web.dtos.Role;
import edu.springboot.organizer.web.dtos.UserDto;
import edu.springboot.organizer.web.services.UserSecuredService;
import edu.springboot.organizer.web.wrappers.UserData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * <a href="https://www.baeldung.com/registration-with-spring-mvc-and-spring-security">
 * The Registration Process With Spring Security</a>
 * <p>
 * <a href="https://www.javaguides.net/2018/10/user-registration-module-using-springboot-springmvc-springsecurity-hibernate5-thymeleaf-mysql.html">
 * Spring Boot User Registration and Login Example Tutorial</a>
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserSecuredService userService;

    // handler method to handle home page request
    @GetMapping("/index")
    public String home() {
        return "index";
    }

    // handler method to handle user registration form request
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // create model object to store form data
        UserData userData = UserData.builder().build();
        model.addAttribute("userData", userData);
        return "register";
    }


    // handler method to handle user registration form submit request
    @PostMapping("/register/save")
    public String registration(@ModelAttribute("userData") UserData userData,
                               Model model) {

        userService.checkUserData(userData);
        userData.getCredentialData().setRole(Role.ROLE_USER);
        userData.validate();

        if (userData.getErrorMessages() != null && !userData.getErrorMessages().isEmpty()) {
            model.addAttribute("errorMessages", userData.getErrorList());
            model.addAttribute("userData", userData);
            return "/register";
        }

        UserDto securedUser = userService.createSecuredUser(userData);
        log.info("User registered [{}]", securedUser.getName());
        return "redirect:/register?success";
    }

    // handler method to handle login request
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
