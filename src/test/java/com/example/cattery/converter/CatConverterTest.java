package com.example.cattery.converter;

import com.example.cattery.dto.CatDTO;
import com.example.cattery.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CatConverterTest {

    private CatConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CatConverter(new BreedConverter());
    }

    @Test
    void convert() {
        Cat cat = new Cat();
        cat.setPrice(1);
        cat.setGender(Gender.MALE);
        cat.setEms("ems");
        cat.setColour("colour");
        cat.setStatus(CatStatus.AVAILABLE);
        cat.setCatClass(CatClass.EXCLUSIVE);
        cat.setBirthDate(LocalDate.now());
        cat.setName("name");
        cat.setId(1L);
        cat.setBreed(new Breed());
        cat.setImages(Arrays.asList(new Byte[]{1, 2, 3, 4}, new Byte[]{1, 2, 3, 4, 5}));

        CatDTO catDTO = converter.convert(cat);
        assertNotNull(catDTO);
        assertEquals(cat.getPrice(), catDTO.getPrice());
        assertEquals(cat.getGender(), catDTO.getGender());
        assertEquals(cat.getEms(), catDTO.getEms());
        assertEquals(cat.getCatClass(), catDTO.getCatClass());
        assertEquals(cat.getColour(), catDTO.getColour());
        assertEquals(cat.getStatus(), catDTO.getStatus());
        assertEquals(cat.getBirthDate(), catDTO.getBirthDate());
        assertEquals(cat.getName(), catDTO.getName());
        assertEquals(cat.getId(), catDTO.getId());
        assertEquals(cat.getBreed(), new BreedDTOConverter().convert(catDTO.getBreed()));
        assertEquals(cat.getImages().size(), catDTO.getImages().size());
        for (int i = 0; i < cat.getImages().size(); i++) {
            assertArrayEquals(cat.getImages().get(i), catDTO.getImages().get(i));
        }
    }

    @Test
    void convertNull() {
        assertNull(converter.convert(null));
    }
}