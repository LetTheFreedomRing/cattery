package com.example.cattery.service;

import com.example.cattery.model.User;

import java.util.Set;

public interface UserService extends CrudService<User, Long> {

    Set<User> getByName(String name);
}
