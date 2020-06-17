package com.example.cattery.controller;

import com.example.cattery.model.Breed;
import com.example.cattery.model.Cat;
import com.example.cattery.repository.BreedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class IndexControllerTest {

    @Mock
    private BreedRepository breedRepository;

    @InjectMocks
    private IndexController indexController;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(indexController).build();
    }

    @Test
    void getIndexPage() throws Exception {
        // given
        Breed breed = new Breed();
        breed.getCats().addAll(Arrays.asList(new Cat(), new Cat(), new Cat())); // added 3 cats

        Mockito.when(breedRepository.findAll()).thenReturn(Stream.of(breed).collect(Collectors.toSet()));

        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("limitCats"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("breeds"));
        Mockito.verify(breedRepository, Mockito.times(1)).findAll();
    }
}