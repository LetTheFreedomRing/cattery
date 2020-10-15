package com.example.cattery.controller;

import com.example.cattery.dto.PasswordDTO;
import com.example.cattery.dto.UserDTO;
import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.exceptions.TokenExpiredException;
import com.example.cattery.exceptions.UserAlreadyExistException;
import com.example.cattery.model.User;
import com.example.cattery.model.VerificationToken;
import com.example.cattery.registration.OnRegistrationCompleteEvent;
import com.example.cattery.security.UserSecurityService;
import com.example.cattery.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final UserSecurityService securityService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final JavaMailSender mailSender;

    public UserController(UserService userService, UserSecurityService securityService,
                          ApplicationEventPublisher applicationEventPublisher, JavaMailSender mailSender) {
        this.userService = userService;
        this.securityService = securityService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.mailSender = mailSender;
    }

    @GetMapping("/view")
    public String getUserPage(Model model) {
        model.addAttribute("user", userService.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "user/view";
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

    @GetMapping("/changePassword")
    public String getChangePasswordPage(@RequestParam("token") String token, Model model) {
        try {
            securityService.validatePasswordResetToken(token);
        } catch (NotFoundException nfe) {
            model.addAttribute("error", "Invalid token");
            return "redirect:/user/login";
        } catch (TokenExpiredException tee) {
            model.addAttribute("error", "Token expired");
            return "redirect:/user/login";
        }
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setToken(token);
        model.addAttribute("password", passwordDTO);
        return "user/resetPassword";
    }

    @GetMapping("/forgotPassword")
    public String getForgotPasswordPage() {
        return "user/forgotPassword";
    }

    @GetMapping("/updatePassword")
    public String getUpdatePasswordPage() {
        return "user/updatePassword";
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
            log.debug("Verification token not found", ex);
            model.addAttribute("message", "Invalid token.");
            return "redirect:/user/bad";
        }
    }

    @PostMapping("/resetPassword")
    public String resetPassword(HttpServletRequest request, @RequestParam("email") String email) {
        final User user = userService.getByEmail(email);

        final String token = UUID.randomUUID().toString();
        securityService.createPasswordResetToken(user, token);
        try {
            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            final SimpleMailMessage mail = constructResetTokenEmail(appUrl, token, user);
            mailSender.send(mail);
        } catch (final MailAuthenticationException e) {
            log.debug("MailAuthenticationException", e);
            return "errors/emailError";
        } catch (final Exception e) {
            log.debug(e.getLocalizedMessage(), e);
            return "redirect:/user/login";
        }
        return "redirect:/user/login";
    }

    @PostMapping("/updatePassword")
    @PreAuthorize("hasPrivilege('READ_PRIVILEGE') " +
            "|| hasPrivilege('WRITE_PRIVILEGE')")
    public String updatePassword(@RequestParam(name = "newPassword") String newPassword,
                                 @RequestParam(name = "oldPassword") String oldPassword,
                                 @RequestParam(name = "confirmPassword") String confirmPassword, Model model) {

        User user = userService.getByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName());

        if (!userService.checkIfValidOldPassword(user, oldPassword)) {
            throw new RuntimeException("Invalid old password");
        }

        if (!(newPassword.equals(confirmPassword))) {
            throw new RuntimeException("New and confirm passwords don't match");
        }

        userService.changePassword(user, newPassword);

        return "redirect:/user/view";
    }

    @PostMapping("/changePassword")
    public String changePassword(@Valid PasswordDTO passwordDTO, Model model) {
        try {
            securityService.validatePasswordResetToken(passwordDTO.getToken());
            User user = securityService.getUserByPasswordResetToken(passwordDTO.getToken());
            userService.changePassword(user, passwordDTO.getNewPassword());
            return "redirect:/user/login";
        } catch (NotFoundException nfe) {
            log.debug("Password reset token token not found", nfe);
            model.addAttribute("error", "Invalid token");
            return "redirect:/user/changePassword";
        } catch (TokenExpiredException tee) {
            log.debug("Token expired", tee);
            model.addAttribute("error", "Token expired");
            return "redirect:/user/changePassword";
        }
    }

    private SimpleMailMessage constructResetTokenEmail(String appUrl, String token, User user) {
        String url = appUrl + "/user/changePassword?token=" + token;
        return constructEmail("Reset Password", "Please follow this url to reset your password" + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body,
                                             User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        return email;
    }
}
