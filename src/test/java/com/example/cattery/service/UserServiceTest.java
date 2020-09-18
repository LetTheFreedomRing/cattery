package com.example.cattery.service;

import com.example.cattery.dto.UserDTO;
import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.exceptions.UserAlreadyExistException;
import com.example.cattery.model.Role;
import com.example.cattery.model.User;
import com.example.cattery.model.VerificationToken;
import com.example.cattery.repository.RoleRepository;
import com.example.cattery.repository.UserRepository;
import com.example.cattery.repository.VerificationTokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userRepository, tokenRepository, roleRepository, passwordEncoder);
    }

    @Test
    void getByName() {
        // given
        User user = new User();
        Mockito.when(userRepository.findByName(ArgumentMatchers.anyString())).thenReturn(Collections.singletonList(user));

        // when
        Set<User> users = userService.getByName("blabla");

        // then
        assertEquals(1, users.size());
        assertEquals(user, users.iterator().next());
    }

    @Test
    void getById() {
        // given
        User user = new User();
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));

        // when
        User foundUser = userService.getById(1L);

        // then
        assertEquals(user, foundUser);
    }

    @Test
    void getByIdThrowsException() {
        // given
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> userService.getById(1L));
    }

    @Test
    void create() {
        // given
        User user = new User();
        Role role = new Role();
        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);
        Mockito.when(roleRepository.findByName(ArgumentMatchers.anyString())).thenReturn(Optional.of(role));

        // when
        User savedUser = userService.registerNewAccount(new UserDTO());

        // then
        assertEquals(user, savedUser);
    }

    @Test
    void registerNewAccount() {
        // given
        final User user = new User();
        Role role = new Role();
        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);
        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findByName(ArgumentMatchers.anyString())).thenReturn(Optional.of(role));

        // when
        final UserDTO userDTO = new UserDTO();
        userDTO.setEmail("email");
        User savedUser = userService.registerNewAccount(userDTO);

        // then
        assertEquals(user, savedUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any());
    }

    @Test
    void registerNewAccountEmailExists() {
        // given
        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(new User()));

        // then
        final UserDTO userDTO = new UserDTO();
        userDTO.setEmail("email");
        assertThrows(UserAlreadyExistException.class, () -> userService.registerNewAccount(userDTO));
    }

    @Test
    void getUser() {
        // given
        User user = new User();
        VerificationToken token = new VerificationToken();
        token.setUser(user);
        Mockito.when(tokenRepository.findByToken(ArgumentMatchers.anyString())).thenReturn(Optional.of(token));

        // when
        User foundUser = userService.getUser("token");

        // then
        assertEquals(user, foundUser);
    }

    @Test
    void getUserNotFound() {
        // given
        Mockito.when(tokenRepository.findByToken(ArgumentMatchers.anyString())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> userService.getUser("token"));
    }

    @Test
    void getVerificationToken() {
        // given
        VerificationToken token = new VerificationToken();
        Mockito.when(tokenRepository.findByToken(ArgumentMatchers.anyString())).thenReturn(Optional.of(token));

        // when
        VerificationToken foundToken = userService.getVerificationToken("token");

        // then
        assertEquals(token, foundToken);
    }

    @Test
    void getVerificationTokenNotFound() {
        // given
        Mockito.when(tokenRepository.findByToken(ArgumentMatchers.anyString())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> userService.getVerificationToken("token"));
    }

    @Test
    void saveRegisteredUser() {
        // when
        userService.saveRegisteredUser(new User());

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any());
    }

    @Test
    void createVerificationToken() {
        // when
        userService.createVerificationToken(new User(), "token");

        // then
        Mockito.verify(tokenRepository, Mockito.times(1)).save(ArgumentMatchers.any());
    }
}