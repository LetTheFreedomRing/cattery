package com.example.cattery.controller;

import com.example.cattery.repository.BreedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class IndexController {

    // indicates how many cats are displayed for each breed on index page
    private static final Integer CATS_LIMIT = 2;

    private final BreedRepository breedRepository;

    public IndexController(BreedRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    @GetMapping({"/", "/index", "/index.html"})
    public String getIndexPage(Model model) {
        model.addAttribute("limitCats", CATS_LIMIT);
        model.addAttribute("breeds", breedRepository.findAll());
        return "index";
    }
}
