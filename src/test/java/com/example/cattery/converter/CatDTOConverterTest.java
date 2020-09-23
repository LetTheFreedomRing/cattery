package com.example.cattery.converter;

import com.example.cattery.dto.BreedDTO;
import com.example.cattery.dto.CatDTO;
import com.example.cattery.model.Cat;
import com.example.cattery.model.CatClass;
import com.example.cattery.model.CatStatus;
import com.example.cattery.model.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CatDTOConverterTest {

    private CatDTOConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CatDTOConverter(new BreedDTOConverter());
    }

    @Test
    void convert() {
        CatDTO catDTO = new CatDTO();
        catDTO.setPrice(1);
        catDTO.setGender(Gender.MALE);
        catDTO.setEms("ems");
        catDTO.setColour("colour");
        catDTO.setStatus(CatStatus.AVAILABLE);
        catDTO.setCatClass(CatClass.EXCLUSIVE);
        catDTO.setBirthDate(LocalDate.now());
        catDTO.setName("name");
        catDTO.setId(1L);
        catDTO.setBreed(new BreedDTO());
        catDTO.setImages(Arrays.asList(new Byte[]{1, 2, 3, 4}, new Byte[]{1, 2, 3, 4, 5}));

        Cat cat = converter.convert(catDTO);
        assertNotNull(cat);
        assertEquals(catDTO.getPrice(), cat.getPrice());
        assertEquals(catDTO.getGender(), cat.getGender());
        assertEquals(catDTO.getEms(), cat.getEms());
        assertEquals(catDTO.getCatClass(), cat.getCatClass());
        assertEquals(catDTO.getColour(), cat.getColour());
        assertEquals(catDTO.getStatus(), cat.getStatus());
        assertEquals(catDTO.getBirthDate(), cat.getBirthDate());
        assertEquals(catDTO.getName(), cat.getName());
        assertEquals(catDTO.getId(), cat.getId());
        assertEquals(catDTO.getBreed(), new BreedConverter().convert(cat.getBreed()));
        assertEquals(catDTO.getImages().size(), cat.getImages().size());
        for (int i = 0; i < catDTO.getImages().size(); i++) {
            assertArrayEquals(catDTO.getImages().get(i), cat.getImages().get(i));
        }
    }

    @Test
    void convertNull() {
        assertNull(converter.convert(null));
    }
}