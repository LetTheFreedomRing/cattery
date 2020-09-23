package com.example.cattery.converter;

import com.example.cattery.dto.UserDTO;
import com.example.cattery.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOConverterTest {

    private UserDTOConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UserDTOConverter();
    }

    @Test
    void convert() {
        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setName("name");
        dto.setPassword("password");
        dto.setEmail("somemail@mail.com");

        User user = converter.convert(dto);
        assertNotNull(user);
        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getPassword(), user.getPassword());
    }

    @Test
    void convertNull() {
        assertNull(converter.convert(null));
    }
}