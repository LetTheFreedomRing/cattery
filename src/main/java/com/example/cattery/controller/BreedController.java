package com.example.cattery.controller;

import com.example.cattery.service.BreedService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/breed")
public class BreedController {

    private final BreedService breedService;

    public BreedController(BreedService breedService) {
        this.breedService = breedService;
    }

    @GetMapping("/{breedId}")
    public String getBreedPage(@PathVariable(name = "breedId") Long breedId, Model model) {
        model.addAttribute("breed", breedService.getById(breedId));
        return "breed";
    }
}
