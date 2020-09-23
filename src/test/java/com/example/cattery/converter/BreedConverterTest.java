package com.example.cattery.converter;

import com.example.cattery.dto.BreedDTO;
import com.example.cattery.model.Breed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BreedConverterTest {

    private BreedConverter converter;

    @BeforeEach
    void setUp() {
        converter = new BreedConverter();
    }

    @Test
    void convert() {
        Breed breed = new Breed();
        breed.setName("name");
        breed.setImage(new Byte[]{1, 2, 3, 4, 5, 6});
        breed.setOverview("overview");
        breed.setHistory("history");
        breed.setTemper("temper");
        breed.setCare("care");
        breed.setId(1L);

        BreedDTO dto = converter.convert(breed);

        assertNotNull(dto);
        assertEquals(breed.getId(), dto.getId());
        assertEquals(breed.getTemper(), dto.getTemper());
        assertEquals(breed.getCare(), dto.getCare());
        assertEquals(breed.getHistory(), dto.getHistory());
        assertEquals(breed.getName(), dto.getName());
        assertEquals(breed.getOverview(), dto.getOverview());
        assertArrayEquals(breed.getImage(), dto.getImage());
    }

    @Test
    void convertNull() {
        assertNull(converter.convert(null));
    }
}