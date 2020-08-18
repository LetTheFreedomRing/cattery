package com.example.cattery.service;

import com.example.cattery.model.Cat;
import com.example.cattery.repository.CatRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CatServiceImpl implements CatService {

    private final CatRepository catRepository;

    public CatServiceImpl(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    @Override
    public Set<Cat> getAll() {
        Set<Cat> cats = new HashSet<>();
        catRepository.findAll().forEach(cats::add);
        return cats;
    }

    @Override
    public Cat getById(Long id) {
        // todo: throw not found exception
        return catRepository.findById(id).orElseThrow(NullPointerException::new);
    }

    @Override
    public void delete(Cat cat) {
        catRepository.delete(cat);
    }

    @Override
    public void deleteById(Long id) {
        catRepository.deleteById(id);
    }

    @Override
    public Cat create(Cat cat) {
        // todo: add default image for cat if not exists
        return catRepository.save(cat);
    }
}
