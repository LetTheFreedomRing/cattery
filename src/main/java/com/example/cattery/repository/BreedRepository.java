package com.example.cattery.repository;

import com.example.cattery.model.Breed;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BreedRepository extends CrudRepository<Breed, Long> {

    Optional<Breed> findByName(String name);
}
