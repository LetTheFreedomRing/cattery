package com.example.cattery.converter;

import com.example.cattery.dto.CatDTO;
import com.example.cattery.dto.CommentDTO;
import com.example.cattery.dto.UserDTO;
import com.example.cattery.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CommentDTOConverterTest {

    private CommentDTOConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CommentDTOConverter(new CatDTOConverter(new BreedDTOConverter()), new UserDTOConverter());
    }

    @Test
    void convert() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCat(new CatDTO());
        commentDTO.setDate(LocalDate.now());
        commentDTO.setUser(new UserDTO());
        commentDTO.setId(1L);
        commentDTO.setMessage("message");

        Comment comment = converter.convert(commentDTO);

        assertNotNull(comment);
        assertEquals(commentDTO.getId(), comment.getId());
        assertEquals(commentDTO.getMessage(), comment.getMessage());
        assertEquals(commentDTO.getDate(), comment.getDate());
        assertEquals(commentDTO.getUser(), new UserConverter().convert(comment.getUser()));
        assertEquals(commentDTO.getCat(), new CatConverter(new BreedConverter()).convert(comment.getCat()));
    }

    @Test
    void convertNull() {
        assertNull(converter.convert(null));
    }
}