package com.example.cattery.service;

import com.example.cattery.dto.BreedDTO;
import com.example.cattery.model.Breed;

import java.util.Set;

public interface BreedService {

    Breed getByName(String name);

    Breed getById(Long id);

    BreedDTO getDTOById(Long id);

    Breed create(BreedDTO breedDTO);

    Set<Breed> getAll();

    Set<BreedDTO> getAllDTOs();

    void deleteById(Long id);
}
