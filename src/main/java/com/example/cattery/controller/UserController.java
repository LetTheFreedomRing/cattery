package com.example.cattery.controller;

import com.example.cattery.dto.UserDTO;
import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.exceptions.UserAlreadyExistException;
import com.example.cattery.model.User;
import com.example.cattery.model.VerificationToken;
import com.example.cattery.registration.OnRegistrationCompleteEvent;
import com.example.cattery.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final ApplicationEventPublisher applicationEventPublisher;

    public UserController(UserService userService, ApplicationEventPublisher applicationEventPublisher) {
        this.userService = userService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user/registration";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "user/login";
    }

    @PostMapping("/")
    public String register(@Valid @ModelAttribute("user") UserDTO userDTO, HttpServletRequest request,
                           BindingResult result, Model model) {

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> {
                log.error(error.toString());
            });
            return "user/registration";
        }
        try {
            User savedUser = userService.registerNewAccount(userDTO);
            log.debug("User : " + savedUser.getId() + " created");
            String appUrl = request.getContextPath();
            applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(savedUser, appUrl));
            return "redirect:/user/login";
        } catch (UserAlreadyExistException uaeEx) {
            log.error("Account already exists");
            model.addAttribute("message", "An account for that username/email already exists");
            return  "user/registration";
        } catch (RuntimeException ex) {
            log.error(ex.toString());
            model.addAttribute("user", userDTO);
            return "errors/emailError";
        }
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration
            (Model model, @RequestParam("token") String token) {

        try {
            final VerificationToken verificationToken = userService.getVerificationToken(token);
            User user = verificationToken.getUser();
            Calendar cal = Calendar.getInstance();
            if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
                model.addAttribute("message", "Your registration token has expired. Please register again.");
                return "redirect:/user/bad";
            }

            user.setEnabled(true);
            userService.saveRegisteredUser(user);
            return "redirect:/user/login";
        } catch (NotFoundException ex) {
            model.addAttribute("message", "Invalid token.");
            return "redirect:/user/bad";
        }
    }
}
