package com.example.cattery.service;

import java.util.Set;

public interface CrudService<T, ID> {

    Set<T> getAll();

    T getById(ID id);

    void delete(T object);

    void deleteById(ID id);

    T create(T object);
}
