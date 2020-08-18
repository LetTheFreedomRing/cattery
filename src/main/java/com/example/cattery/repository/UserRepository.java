package com.example.cattery.repository;

import com.example.cattery.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface UserRepository extends CrudRepository<User, Long> {

    Collection<User> findByName(String name);
}
