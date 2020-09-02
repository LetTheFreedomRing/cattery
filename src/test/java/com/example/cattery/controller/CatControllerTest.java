package com.example.cattery.controller;

import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.model.Cat;
import com.example.cattery.service.CatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

class CatControllerTest {

    @Mock
    private CatService catService;

    @InjectMocks
    private CatController catController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        catController = new CatController(catService);
        mockMvc = MockMvcBuilders.standaloneSetup(catController).build();
    }

    @Test
    void getCatPage() throws Exception {
        // given
        Cat cat = new Cat();
        Mockito.when(catService.getById(ArgumentMatchers.anyLong())).thenReturn(cat);

        mockMvc.perform(MockMvcRequestBuilders.get("/cat/{catId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cat"))
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
}