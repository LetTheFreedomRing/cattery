package com.example.cattery.controller;

import com.example.cattery.Utils;
import com.example.cattery.model.Breed;
import com.example.cattery.service.BreedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@Slf4j
@RequestMapping("/breed")
public class BreedController {

    private final BreedService breedService;

    public BreedController(BreedService breedService) {
        this.breedService = breedService;
    }

    @GetMapping("/{breedId}")
    public String getBreedPage(@PathVariable(name = "breedId") Long breedId, Model model) {
        model.addAttribute("breed", breedService.getById(breedId));
        return "breed/view";
    }

    @GetMapping("/create")
    public String createBreedPage(Model model) {
        return createOrUpdate(new Breed(), model);
    }

    @GetMapping("/{breedId}/edit")
    public String updateBreedPage(@PathVariable("breedId") Long breedId, Model model) {
        return createOrUpdate(breedService.getById(breedId), model);
    }

    private String createOrUpdate(Breed breed, Model model) {
        model.addAttribute("breed", breed);
        return "breed/new";
    }

    @PostMapping("/")
    public String createOrUpdate(@ModelAttribute("breed") Breed breed, @RequestParam("image_file") MultipartFile image, BindingResult result) {
        if (!image.isEmpty()) {
            try {
                breed.setImage(Utils.convert(image.getBytes()));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        Breed savedBreed = breedService.create(breed);
        return "redirect:/breed/" + savedBreed.getId();
    }

    @GetMapping("/{breedId}/delete")
    public String delete(@PathVariable("breedId") Long breedId) {
        breedService.deleteById(breedId);
        return "redirect:/";
    }
}
