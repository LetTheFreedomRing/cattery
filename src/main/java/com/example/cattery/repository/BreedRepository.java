package com.example.cattery.repository;

import com.example.cattery.model.Breed;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface BreedRepository extends CrudRepository<Breed, Long> {

    Collection<Breed> findByName(String name);
}
