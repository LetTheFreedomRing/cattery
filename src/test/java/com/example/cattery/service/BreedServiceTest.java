package com.example.cattery.service;

import com.example.cattery.converter.BreedConverter;
import com.example.cattery.converter.BreedDTOConverter;
import com.example.cattery.dto.BreedDTO;
import com.example.cattery.exceptions.BreedAlreadyExistException;
import com.example.cattery.exceptions.NotFoundException;
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

    @Mock
    private BreedImageService breedImageService;

    @Mock
    private BreedConverter breedConverter;

    @Mock
    private BreedDTOConverter breedDTOConverter;

    @InjectMocks
    private BreedServiceImpl breedService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        breedService = new BreedServiceImpl(breedRepository, breedImageService, breedConverter, breedDTOConverter);
    }

    @Test
    void getByName() {
        // given
        String breedName = "blabla";
        Breed breed = new Breed();
        breed.setName(breedName);
        Mockito.when(breedRepository.findByName(ArgumentMatchers.anyString())).thenReturn(Optional.of(breed));

        // when
        Breed foundBreed = breedService.getByName(breedName);

        // then
        assertEquals(breed, foundBreed);
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
    void getAllDTOs() {
        // given
        Breed breed = new Breed();
        BreedDTO breedDTO = new BreedDTO();
        Mockito.when(breedRepository.findAll()).thenReturn(Collections.singletonList(breed));
        Mockito.when(breedConverter.convert(ArgumentMatchers.any())).thenReturn(breedDTO);

        // when
        Set<BreedDTO> breeds = breedService.getAllDTOs();

        // then
        assertEquals(1, breeds.size());
        assertEquals(breedDTO, breeds.iterator().next());
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
    void getDTOById() {
        // given
        Breed breed = new Breed();
        BreedDTO breedDTO = new BreedDTO();
        Mockito.when(breedRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(breed));
        Mockito.when(breedConverter.convert(ArgumentMatchers.any())).thenReturn(breedDTO);

        // when
        BreedDTO foundBreed = breedService.getDTOById(1L);

        // then
        assertEquals(breedDTO, foundBreed);
    }

    @Test
    void getByIdThrowsException() {
        // given
        Mockito.when(breedRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> breedService.getById(1L));
    }

    @Test
    void getByDTOIdThrowsException() {
        // given
        Mockito.when(breedRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> breedService.getDTOById(1L));
    }

    @Test
    void createNewBreed() {
        // given
        Breed breed = new Breed();
        Mockito.when(breedRepository.save(ArgumentMatchers.any())).thenReturn(breed);

        // when
        Breed savedBreed = breedService.create(new BreedDTO());

        // then
        assertEquals(breed, savedBreed);
    }

    @Test
    void createNewBreedAlreadyExists() {
        // given
        BreedDTO breedDTO = new BreedDTO();
        breedDTO.setName("name");
        Mockito.when(breedRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.of(new Breed()));

        // then
        assertThrows(BreedAlreadyExistException.class, () -> breedService.create(breedDTO));
    }

    @Test
    void createBreedAlreadyExistsSameIds() {
        // given
        BreedDTO breedDTO = new BreedDTO();
        breedDTO.setName("name");
        breedDTO.setId(1L);
        Breed breed = new Breed();
        breed.setId(1L);
        Mockito.when(breedRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.of(breed));
        Mockito.when(breedRepository.save(ArgumentMatchers.any())).thenReturn(breed);

        // when
        Breed savedBreed = breedService.create(breedDTO);

        // then
        assertEquals(breed, savedBreed);
    }

    @Test
    void createBreedAlreadyExistsDifferentIds() {
        // given
        BreedDTO breedDTO = new BreedDTO();
        breedDTO.setName("name");
        breedDTO.setId(1L);
        Breed breed = new Breed();
        breed.setId(2L);
        Mockito.when(breedRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.of(breed));
        Mockito.when(breedRepository.save(ArgumentMatchers.any())).thenReturn(breed);

        // then
        assertThrows(BreedAlreadyExistException.class, () -> breedService.create(breedDTO));
    }
}