package com.example.cattery.converter;

import com.example.cattery.dto.BreedDTO;
import com.example.cattery.model.Breed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BreedDTOConverterTest {

    private BreedDTOConverter converter;

    @BeforeEach
    void setUp() {
        converter = new BreedDTOConverter();
    }

    @Test
    void convert() {
        BreedDTO breedDTO = new BreedDTO();
        breedDTO.setName("name");
        breedDTO.setImage(new Byte[]{1, 2, 3, 4, 5, 6});
        breedDTO.setOverview("overview");
        breedDTO.setHistory("history");
        breedDTO.setTemper("temper");
        breedDTO.setCare("care");
        breedDTO.setId(1L);

        Breed breed = converter.convert(breedDTO);

        assertNotNull(breed);
        assertEquals(breedDTO.getId(), breed.getId());
        assertEquals(breedDTO.getTemper(), breed.getTemper());
        assertEquals(breedDTO.getCare(), breed.getCare());
        assertEquals(breedDTO.getHistory(), breed.getHistory());
        assertEquals(breedDTO.getName(), breed.getName());
        assertEquals(breedDTO.getOverview(), breed.getOverview());
        assertArrayEquals(breedDTO.getImage(), breed.getImage());
    }

    @Test
    void convertNull() {
        assertNull(converter.convert(null));
    }
}