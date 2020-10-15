package com.example.cattery.controller;

import com.example.cattery.Utils;
import com.example.cattery.dto.BreedDTO;
import com.example.cattery.exceptions.BreedAlreadyExistException;
import com.example.cattery.model.Breed;
import com.example.cattery.service.BreedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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
        return createOrUpdate(new BreedDTO(), model);
    }

    @GetMapping("/{breedId}/edit")
    public String updateBreedPage(@PathVariable("breedId") Long breedId, Model model) {
        return createOrUpdate(breedService.getDTOById(breedId), model);
    }

    private String createOrUpdate(BreedDTO breedDTO, Model model) {
        model.addAttribute("breed", breedDTO);
        return "breed/new";
    }

    @PostMapping("/")
    public String createOrUpdate(@Valid @ModelAttribute("breed") BreedDTO breedDTO, BindingResult result, @RequestParam("image_file") MultipartFile image) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> log.error(error.toString()));
            return "breed/new";
        }
        if (!image.isEmpty()) {
            try {
                breedDTO.setImage(Utils.convert(image.getBytes()));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        try {
            Breed savedBreed = breedService.create(breedDTO);
            return "redirect:/breed/" + savedBreed.getId();
        } catch (BreedAlreadyExistException ex) {
            result.rejectValue("name", "duplicate", "already exists");
            return "breed/new";
        }
    }

    @GetMapping("/{breedId}/delete")
    public String delete(@PathVariable("breedId") Long breedId) {
        breedService.deleteById(breedId);
        return "redirect:/";
    }
}
