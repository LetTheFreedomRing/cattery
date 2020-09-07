package com.example.cattery.controller;

import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.model.Breed;
import com.example.cattery.service.BreedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

class BreedControllerTest {

    private static final Long BREED_ID = 1L;
    private static final String BREED_OVERVIEW = "Breed overview";
    private static final String BREED_HISTORY = "Breed history";
    private static final String BREED_CARE = "Breed care";
    private static final String BREED_TEMPER = "Breed temper";
    private static final String BREED_NAME = "Breed name";

    @Mock
    private BreedService breedService;

    @InjectMocks
    private BreedController breedController;

    private Breed breed;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        breedController = new BreedController(breedService);
        breed = new Breed();
        breed.setCare(BREED_CARE);
        breed.setTemper(BREED_TEMPER);
        breed.setHistory(BREED_HISTORY);
        breed.setName(BREED_NAME);
        breed.setId(BREED_ID);
        breed.setOverview(BREED_OVERVIEW);
        mockMvc = MockMvcBuilders.standaloneSetup(breedController).build();
    }

    @Test
    void getBreedPage() throws Exception {
        // given
        Mockito.when(breedService.getById(ArgumentMatchers.anyLong())).thenReturn(breed);

        mockMvc.perform(MockMvcRequestBuilders.get("/breed/{breedId}", BREED_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("breed/view"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("breed"));
        Mockito.verify(breedService, Mockito.times(1)).getById(ArgumentMatchers.anyLong());
    }

    @Test
    void getBreedPageIdNotFound() throws Exception {
        // given
        Mockito.when(breedService.getById(ArgumentMatchers.anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/breed/{breedId}", BREED_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        Mockito.verify(breedService, Mockito.times(1)).getById(ArgumentMatchers.anyLong());
    }
}