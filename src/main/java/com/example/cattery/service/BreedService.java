package com.example.cattery.service;

import com.example.cattery.model.Breed;

import java.util.Set;

public interface BreedService extends CrudService<Breed, Long> {

    Set<Breed> getByName(String name);
}
