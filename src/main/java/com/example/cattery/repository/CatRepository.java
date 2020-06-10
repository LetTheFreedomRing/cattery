package com.example.cattery.repository;

import com.example.cattery.model.Cat;
import org.springframework.data.repository.CrudRepository;

public interface CatRepository extends CrudRepository<Cat, Long> {
}
