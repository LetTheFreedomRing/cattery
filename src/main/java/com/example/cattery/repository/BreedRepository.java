package com.example.cattery.repository;

import com.example.cattery.model.Breed;
import org.springframework.data.repository.CrudRepository;

public interface BreedRepository extends CrudRepository<Breed, Long> {
}
