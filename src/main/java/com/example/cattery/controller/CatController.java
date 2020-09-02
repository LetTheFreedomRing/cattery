package com.example.cattery.controller;

import com.example.cattery.service.CatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("cat")
public class CatController {

    private final CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    @GetMapping("/{catId}")
    public String getCatPage(@PathVariable(name = "catId") Long catId, Model model) {
        model.addAttribute("cat", catService.getById(catId));
        return "cat";
    }
}
