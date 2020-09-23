package com.example.cattery.converter;

import com.example.cattery.dto.UserDTO;
import com.example.cattery.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserConverterTest {

    private UserConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UserConverter();
    }

    @Test
    void convert() {
        User user = new User();
        user.setId(1L);
        user.setName("name");
        user.setPassword("password");
        user.setEmail("somemail@mail.com");

        UserDTO dto = converter.convert(user);
        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getPassword(), dto.getPassword());
        assertEquals(dto.getPassword(), dto.getMatchingPassword());
    }

    @Test
    void convertNull() {
        assertNull(converter.convert(null));
    }
}