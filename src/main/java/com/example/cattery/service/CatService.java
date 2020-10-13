package com.example.cattery.service;

import com.example.cattery.dto.CatDTO;
import com.example.cattery.model.Cat;
import com.example.cattery.model.User;

public interface CatService {

    Cat getById(Long id);

    CatDTO getDTOById(Long id);

    Cat create(CatDTO cat);

    void deleteById(Long catId);

    void updateOwner(User user, Cat cat);
}
