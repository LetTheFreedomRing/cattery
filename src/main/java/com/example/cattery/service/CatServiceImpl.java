package com.example.cattery.service;

import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.model.Cat;
import com.example.cattery.repository.CatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class CatServiceImpl implements CatService {

    private final CatRepository catRepository;

    private final CatImageService catImageService;

    public CatServiceImpl(CatRepository catRepository, CatImageService catImageService) {
        this.catRepository = catRepository;
        this.catImageService = catImageService;
    }

    @Override
    public Set<Cat> getAll() {
        Set<Cat> cats = new HashSet<>();
        catRepository.findAll().forEach(cats::add);
        return cats;
    }

    @Override
    public Cat getById(Long id) {
        return catRepository.findById(id).orElseThrow(NotFoundException::new);
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
        if (cat.getImages().size() == 0) {
            Byte[] image = catImageService.getDefaultImageBytes();
            cat.getImages().add(image);
        }
        cat.setLastUpdated(LocalDate.now());
        return catRepository.save(cat);
    }
}
