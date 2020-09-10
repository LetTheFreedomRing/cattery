package com.example.cattery.repository;

import com.example.cattery.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Collection<User> findByName(String name);

    Optional<User> findByEmail(String email);
}
