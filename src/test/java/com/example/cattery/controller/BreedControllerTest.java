package com.example.cattery.controller;

import com.example.cattery.dto.BreedDTO;
import com.example.cattery.exceptions.BreedAlreadyExistException;
import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.model.Breed;
import com.example.cattery.service.BreedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
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

    @Test
    void createBreedPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/breed/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("breed/new"))
                .andExpect(MockMvcResultMatchers.model().attribute("breed", new BreedDTO()));
    }

    @Test
    void updateBreedPage() throws Exception {
        // given
        Mockito.when(breedService.getDTOById(ArgumentMatchers.anyLong())).thenReturn(new BreedDTO());

        mockMvc.perform(MockMvcRequestBuilders.get("/breed/{breedId}/edit", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("breed/new"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("breed"));
    }

    @Test
    void create() throws Exception {
        // given
        Breed breed = new Breed();
        breed.setId(BREED_ID);
        MockMultipartFile file = new MockMultipartFile("image_file", "testing.txt",
                "text/plain", "Dummy".getBytes());
        Mockito.when(breedService.create(ArgumentMatchers.any())).thenReturn(breed);

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/breed/").file(file)
                        .param("name", BREED_NAME)
                        .param("overview", BREED_OVERVIEW)
                        .param("history", BREED_HISTORY)
                        .param("care", BREED_CARE)
                        .param("temper", BREED_TEMPER))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/breed/" + BREED_ID));
        Mockito.verify(breedService, Mockito.times(1)).create(ArgumentMatchers.any());
    }

    @Test
    void createNotValid() throws Exception {
        // given
        Breed breed = new Breed();
        breed.setId(BREED_ID);
        MockMultipartFile file = new MockMultipartFile("image_file", "testing.txt",
                "text/plain", "Dummy".getBytes());
        Mockito.when(breedService.create(ArgumentMatchers.any())).thenReturn(breed);

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/breed/").file(file)
                .param("name", BREED_NAME)
                .param("overview", BREED_OVERVIEW)
                .param("temper", BREED_TEMPER))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("breed", "care"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("breed", "history"))
                .andExpect(MockMvcResultMatchers.view().name("breed/new"));
        Mockito.verify(breedService, Mockito.times(0)).create(ArgumentMatchers.any());
    }

    @Test
    void createNameExists() throws Exception {
        // given
        Breed breed = new Breed();
        breed.setId(BREED_ID);
        MockMultipartFile file = new MockMultipartFile("image_file", "testing.txt",
                "text/plain", "Dummy".getBytes());
        Mockito.when(breedService.create(ArgumentMatchers.any())).thenThrow(BreedAlreadyExistException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/breed/").file(file)
                .param("name", BREED_NAME)
                .param("overview", BREED_OVERVIEW)
                .param("history", BREED_HISTORY)
                .param("care", BREED_CARE)
                .param("temper", BREED_TEMPER))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("breed/new"));
        Mockito.verify(breedService, Mockito.times(1)).create(ArgumentMatchers.any());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/breed/{breedId}/delete", 1L))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"));

        Mockito.verify(breedService, Mockito.times(1)).deleteById(1L);
    }
}