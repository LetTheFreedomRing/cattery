package com.example.cattery.service;

import com.example.cattery.model.Breed;
import com.example.cattery.repository.BreedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BreedServiceTest {

    @Mock
    private BreedRepository breedRepository;

    @InjectMocks
    private BreedServiceImpl breedService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        breedService = new BreedServiceImpl(breedRepository);
    }

    @Test
    void getByName() {
        // given
        String breedName = "blabla";
        Breed breed = new Breed();
        breed.setName(breedName);
        Mockito.when(breedRepository.findByName(ArgumentMatchers.anyString())).thenReturn(Collections.singletonList(breed));

        // when
        Set<Breed> breeds = breedService.getByName(breedName);

        // then
        assertEquals(1, breeds.size());
        assertEquals(breed, breeds.iterator().next());
    }

    @Test
    void getAll() {
        // given
        Breed breed = new Breed();
        Mockito.when(breedRepository.findAll()).thenReturn(Collections.singletonList(breed));

        // when
        Set<Breed> breeds = breedService.getAll();

        // then
        assertEquals(1, breeds.size());
        assertEquals(breed, breeds.iterator().next());
    }

    @Test
    void getById() {
        // given
        Breed breed = new Breed();
        Mockito.when(breedRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(breed));

        // when
        Breed foundBreed = breedService.getById(1L);

        // then
        assertEquals(breed, foundBreed);
    }

    @Test
    void getByIdThrowsException() {
        // given
        Mockito.when(breedRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(NullPointerException.class, () -> breedService.getById(1L));
    }

    @Test
    void create() {
        // given
        Breed breed = new Breed();
        Mockito.when(breedRepository.save(ArgumentMatchers.any())).thenReturn(breed);

        // when
        Breed savedBreed = breedService.create(new Breed());

        // then
        assertEquals(breed, savedBreed);
    }
}