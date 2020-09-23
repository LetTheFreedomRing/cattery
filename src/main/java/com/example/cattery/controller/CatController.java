package com.example.cattery.controller;

import com.example.cattery.Utils;
import com.example.cattery.dto.BreedDTO;
import com.example.cattery.dto.CatDTO;
import com.example.cattery.dto.CommentDTO;
import com.example.cattery.model.Cat;
import com.example.cattery.service.BreedService;
import com.example.cattery.service.CatService;
import com.example.cattery.service.CommentService;
import com.example.cattery.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@Slf4j
@RequestMapping("cat")
public class CatController {

    private final CatService catService;
    private final BreedService breedService;
    private final UserService userService;
    private final CommentService commentService;

    public CatController(CatService catService, BreedService breedService, UserService userService, CommentService commentService) {
        this.catService = catService;
        this.breedService = breedService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping("/{catId}")
    public String getCatPage(@PathVariable(name = "catId") Long catId, Model model) {
        model.addAttribute("cat", catService.getById(catId));
        model.addAttribute("comment", new CommentDTO());
        return "cat/view";
    }

    @GetMapping("/create")
    public String createCatPage(Model model) {
        CatDTO catDTO = new CatDTO();
        catDTO.setBreed(new BreedDTO());
        return createOrUpdate(catDTO, model);
    }

    @GetMapping("/{catId}/edit")
    public String updateCatPage(@PathVariable("catId") Long catId, Model model) {
        return createOrUpdate(catService.getDTOById(catId), model);
    }

    private String createOrUpdate(CatDTO catDTO, Model model) {
        model.addAttribute("cat", catDTO);
        model.addAttribute("breeds", breedService.getAllDTOs());
        return "cat/new";
    }

    @PostMapping("/")
    public String createOrEdit(@ModelAttribute("cat") CatDTO catDTO, @RequestParam("image_files") MultipartFile[] images, BindingResult result) {
        // todo : handle validations
        // add images to cat
        for (MultipartFile image : images) {
            try {
                catDTO.getImages().add(Utils.convert(image.getBytes()));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        Cat savedCat = catService.create(catDTO);
        return "redirect:/cat/" + savedCat.getId();
    }

    @PostMapping("/{catId}/comment")
    public String comment(@PathVariable(name = "catId") Long catId, @ModelAttribute("commentDTO") CommentDTO commentDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // check if authorized
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            // todo : handle better
            throw new RuntimeException("Not authorized");
        }
        commentDTO.setUser(userService.getDTOByEmail(authentication.getName()));
        commentDTO.setCat(catService.getDTOById(catId));
        commentDTO.setDate(LocalDate.now());
        commentService.create(commentDTO);
        return "redirect:/cat/" + catId;
    }

    @GetMapping("/{catId}/delete")
    public String delete(@PathVariable("catId") Long catId) {
        catService.deleteById(catId);
        return "redirect:/";
    }
}
