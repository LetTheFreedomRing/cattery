package com.example.cattery.service;

import com.example.cattery.converter.CatConverter;
import com.example.cattery.converter.CatDTOConverter;
import com.example.cattery.dto.CatDTO;
import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.model.Cat;
import com.example.cattery.repository.CatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CatServiceTest {

    @Mock
    private CatRepository catRepository;

    @Mock
    private CatImageService catImageService;

    @Mock
    private CatConverter catConverter;

    @Mock
    private CatDTOConverter catDTOConverter;

    @InjectMocks
    private CatServiceImpl catService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        catService = new CatServiceImpl(catRepository, catImageService, catConverter, catDTOConverter);
    }

    @Test
    void getById() {
        // given
        Cat cat = new Cat();
        Mockito.when(catRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(cat));

        // when
        Cat foundCat = catService.getById(1L);

        // then
        assertEquals(cat, foundCat);
    }

    @Test
    void getDTOById() {
        // given
        Cat cat = new Cat();
        CatDTO catDTO = new CatDTO();
        Mockito.when(catRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(cat));
        Mockito.when(catConverter.convert(ArgumentMatchers.any())).thenReturn(catDTO);

        // when
        CatDTO foundCat = catService.getDTOById(1L);

        // then
        assertEquals(catDTO, foundCat);
    }

    @Test()
    void getByIdThrowsException() {
        // given
        Mockito.when(catRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> catService.getById(1L));
    }

    @Test()
    void getDTOByIdThrowsException() {
        // given
        Mockito.when(catRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> catService.getDTOById(1L));
    }

    @Test
    void create() {
        // given
        Cat cat = new Cat();
        cat.setId(1L);
        Mockito.when(catRepository.save(ArgumentMatchers.any())).thenReturn(cat);
        Mockito.when(catDTOConverter.convert(ArgumentMatchers.any())).thenReturn(cat);

        // when
        Cat savedCat = catService.create(new CatDTO());

        // then
        assertEquals(cat, savedCat);
    }
}
