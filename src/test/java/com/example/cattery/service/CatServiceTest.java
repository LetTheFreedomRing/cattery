package com.example.cattery.service;

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

    @InjectMocks
    private CatServiceImpl catService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        catService = new CatServiceImpl(catRepository);
    }

    @Test
    void getAll() {
        // given
        Cat cat = new Cat();
        Mockito.when(catRepository.findAll()).thenReturn(Collections.singletonList(cat));

        // when
        Set<Cat> cats = catService.getAll();

        // then
        assertEquals(1, cats.size());
        assertEquals(cat, cats.iterator().next());
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

    @Test()
    void getByIdThrowsException() {
        // given
        Mockito.when(catRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> catService.getById(1L));
    }

    @Test
    void create() {
        // given
        Cat cat = new Cat();
        cat.setId(1L);
        Mockito.when(catRepository.save(ArgumentMatchers.any())).thenReturn(cat);

        // when
        Cat savedCat = catService.create(new Cat());

        // then
        assertEquals(cat, savedCat);
    }
}
