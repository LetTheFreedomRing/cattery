package com.example.cattery.controller;

import com.example.cattery.Utils;
import com.example.cattery.model.Breed;
import com.example.cattery.model.Cat;
import com.example.cattery.service.BreedService;
import com.example.cattery.service.CatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@Slf4j
@RequestMapping("cat")
public class CatController {

    private final CatService catService;
    private final BreedService breedService;

    public CatController(CatService catService, BreedService breedService) {
        this.catService = catService;
        this.breedService = breedService;
    }

    @GetMapping("/{catId}")
    public String getCatPage(@PathVariable(name = "catId") Long catId, Model model) {
        model.addAttribute("cat", catService.getById(catId));
        return "cat/view";
    }

    @GetMapping("/create")
    public String createCatPage(Model model) {
        Cat newCat = new Cat();
        // todo: remove this after creating CatCommand
        newCat.setBreed(new Breed());
        return createOrUpdate(newCat, model);
    }

    @GetMapping("/{catId}/edit")
    public String updateCatPage(@PathVariable("catId") Long catId, Model model) {
        return createOrUpdate(catService.getById(catId), model);
    }

    private String createOrUpdate(Cat cat, Model model) {
        model.addAttribute("cat", cat);
        model.addAttribute("breeds", breedService.getAll());
        return "cat/new";
    }

    @PostMapping("/")
    public String createOrEdit(@ModelAttribute("cat") Cat cat, @RequestParam("image_files") MultipartFile[] images, BindingResult result) {
        // todo : remove this after creating CatCommand
        if (!cat.getBreed().getName().equals("")) {
            cat.setBreed(breedService.getByName(cat.getBreed().getName()).iterator().next());
        }
        // add images to cat
        for (MultipartFile image : images) {
            try {
                cat.getImages().add(Utils.convert(image.getBytes()));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        Cat savedCat = catService.create(cat);
        return "redirect:/cat/" + savedCat.getId();
    }
}
