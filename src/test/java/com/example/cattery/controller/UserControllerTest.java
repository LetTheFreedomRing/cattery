package com.example.cattery.controller;

import com.example.cattery.dto.UserDTO;
import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.exceptions.UserAlreadyExistException;
import com.example.cattery.model.User;
import com.example.cattery.model.VerificationToken;
import com.example.cattery.registration.OnRegistrationCompleteEvent;
import com.example.cattery.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userController = new UserController(userService, eventPublisher);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getRegistrationPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/registration"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", new UserDTO()));
    }

    @Test
    void getLoginPage() throws Exception {
        // todo : write tests as soon as login logic is added
        mockMvc.perform(MockMvcRequestBuilders.get("/user/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/login"));
    }

    @Test
    void register() throws Exception {
        Mockito.when(userService.registerNewAccount(ArgumentMatchers.any())).thenReturn(new User());
        mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                .param("name", "Name")
                .param("email", "email@mail.com")
                .param("password", "password")
                .param("matchingPassword", "password")
            ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user/login"));
        Mockito.verify(userService, Mockito.times(1)).registerNewAccount(ArgumentMatchers.any());
    }

    @Test
    void registerValidationErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                .param("name", "Name")
                .param("email", "")
                .param("password", "password")
                .param("matchingPassword", "passw")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verifyNoInteractions(userService);
    }

    @Test
    void registerUserExists() throws Exception {
        Mockito.when(userService.registerNewAccount(ArgumentMatchers.any())).thenThrow(new UserAlreadyExistException());
        mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                .param("name", "Name")
                .param("email", "email@mail.com")
                .param("password", "password")
                .param("matchingPassword", "password")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/registration"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("message"));
        Mockito.verify(userService, Mockito.times(1)).registerNewAccount(ArgumentMatchers.any());
    }

    @Test
    void registerEmailError() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(eventPublisher).publishEvent(ArgumentMatchers.isA(OnRegistrationCompleteEvent.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/user/")
                .param("name", "Name")
                .param("email", "email@mail.com")
                .param("password", "password")
                .param("matchingPassword", "password")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("errors/emailError"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
        Mockito.verify(userService, Mockito.times(1)).registerNewAccount(ArgumentMatchers.any());
    }

    @Test
    void confirmRegistration() throws Exception {
        VerificationToken token = new VerificationToken("token", new User());
        Mockito.when(userService.getVerificationToken(ArgumentMatchers.anyString())).thenReturn(token);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/registrationConfirm?token=token"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user/login"));
        assertTrue(token.getUser().isEnabled());
        Mockito.verify(userService, Mockito.times(1)).saveRegisteredUser(ArgumentMatchers.any());
    }

    @Test
    void confirmRegistrationInvalidToken() throws Exception {
        Mockito.when(userService.getVerificationToken(ArgumentMatchers.anyString())).thenThrow(new NotFoundException());
        mockMvc.perform(MockMvcRequestBuilders.get("/user/registrationConfirm?token=token"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user/bad"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("message"));
        Mockito.verify(userService, Mockito.times(0)).saveRegisteredUser(ArgumentMatchers.any());
    }

    @Test
    void confirmRegistrationTokenExpired() throws Exception {
        VerificationToken token = new VerificationToken("token", new User());
        Calendar cal = Calendar.getInstance();
        token.setExpiryDate(cal.getTime());
        Mockito.when(userService.getVerificationToken(ArgumentMatchers.anyString())).thenReturn(token);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/registrationConfirm?token=token"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user/bad"));
        assertFalse(token.getUser().isEnabled());
        Mockito.verify(userService, Mockito.times(0)).saveRegisteredUser(ArgumentMatchers.any());
    }
}