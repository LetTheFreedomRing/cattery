package com.example.cattery.controller;

import com.example.cattery.dto.BreedDTO;
import com.example.cattery.dto.CatDTO;
import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.model.*;
import com.example.cattery.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.HashSet;

class CatControllerTest {

    private static final Long CAT_ID = 1L;
    private static final String CAT_NAME = "dummy";
    private static final Gender CAT_GENDER = Gender.MALE;
    private static final String CAT_COLOUR = "grey";
    private static final String CAT_EMS = "ems";
    private static final Integer CAT_PRICE = 1;
    private static final LocalDate CAT_BIRTH_DATE = LocalDate.now();
    private static final CatStatus CAT_STATUS = CatStatus.AVAILABLE;
    private static final CatClass CAT_CLASS = CatClass.EXCLUSIVE;

    @Mock
    private CatService catService;

    @Mock
    private BreedService breedService;

    @Mock
    private UserService userService;

    @Mock
    private CommentService commentService;

    @Mock
    private StripeService stripeService;

    @InjectMocks
    private CatController catController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        catController = new CatController(catService, breedService, userService, commentService, stripeService);
        mockMvc = MockMvcBuilders.standaloneSetup(catController).build();
    }

    @Test
    void getCatPage() throws Exception {
        // given
        Cat cat = new Cat();
        Mockito.when(catService.getById(ArgumentMatchers.anyLong())).thenReturn(cat);

        mockMvc.perform(MockMvcRequestBuilders.get("/cat/{catId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cat/view"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("cat"));
        Mockito.verify(catService, Mockito.times(1)).getById(ArgumentMatchers.anyLong());
    }

    @Test
    void getCatPageNotFound() throws Exception {
        // given
        Cat cat = new Cat();
        Mockito.when(catService.getById(ArgumentMatchers.anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/cat/{catId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        Mockito.verify(catService, Mockito.times(1)).getById(ArgumentMatchers.anyLong());
    }


    @Test
    void createCatPage() throws Exception {
        Mockito.when(breedService.getAll()).thenReturn(new HashSet<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/cat/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cat/new"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("cat"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("breeds"));
    }

    @Test
    void updateCatPage() throws Exception {
        // given
        CatDTO catDTO = new CatDTO();
        catDTO.setBreed(new BreedDTO());
        Mockito.when(breedService.getAll()).thenReturn(new HashSet<>());
        Mockito.when(catService.getDTOById(ArgumentMatchers.anyLong())).thenReturn(catDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/cat/{catId}/edit", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cat/new"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("breeds"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("cat"));
    }

    @Test
    void create() throws Exception {
        // given
        Cat cat = new Cat();
        cat.setId(CAT_ID);
        MockMultipartFile file = new MockMultipartFile("image_files", "testing.txt",
                "text/plain", "Dummy".getBytes());
        Mockito.when(catService.create(ArgumentMatchers.any())).thenReturn(cat);

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/cat/").file(file)
                .param("name", CAT_NAME)
                .param("gender", CAT_GENDER.name())
                .param("colour", CAT_COLOUR)
                .param("ems", CAT_EMS)
                .param("price", String.valueOf(CAT_PRICE))
                .param("birthDate", CAT_BIRTH_DATE.toString())
                .param("status", CAT_STATUS.name())
                .param("catClass", CAT_CLASS.name())
                .param("breed.name", ""))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/cat/" + CAT_ID));
        Mockito.verify(catService, Mockito.times(1)).create(ArgumentMatchers.any());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cat/{catId}/delete", 1L))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"));

        Mockito.verify(catService, Mockito.times(1)).deleteById(1L);
    }
}