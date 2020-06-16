package com.example.cattery.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class IndexController {

    @GetMapping({"/", "/index", "/index.html"})
    public String getIndexPage(Model model) {
        // todo : load breeds and corresponding cats (max 2 for every breed) and add to the model
        return "index";
    }
}
