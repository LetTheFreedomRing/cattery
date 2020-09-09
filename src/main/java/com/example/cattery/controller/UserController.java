package com.example.cattery.controller;

import com.example.cattery.exceptions.UserAlreadyExistException;
import com.example.cattery.model.User;
import com.example.cattery.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "user/registration";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "user/login";
    }

    @PostMapping("/")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> {
                log.error(error.toString());
            });
            return "user/registration";
        }
        try {
            User savedUser = userService.create(user);
            return "redirect:/user/login";
        } catch (UserAlreadyExistException ex) {
            log.error("Account already exists");
            model.addAttribute("message", "An account for that username/email already exists");
            return  "user/registration";
        }
    }
}
