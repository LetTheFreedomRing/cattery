package com.example.cattery.service;

import com.example.cattery.model.User;
import com.example.cattery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userRepository);
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
    void getAll() {
        // given
        User user = new User();
        Mockito.when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        // when
        Set<User> users = userService.getAll();

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
        assertThrows(NullPointerException.class, () -> userService.getById(1L));
    }

    @Test
    void create() {
        // given
        User user = new User();
        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);

        // when
        User savedUser = userService.create(new User());

        // then
        assertEquals(user, savedUser);
    }
}