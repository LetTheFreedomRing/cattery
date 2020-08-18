package com.example.cattery.service;

import com.example.cattery.model.Breed;
import com.example.cattery.repository.BreedRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class BreedServiceImpl implements BreedService {

    private final BreedRepository breedRepository;

    public BreedServiceImpl(BreedRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    @Override
    public Set<Breed> getByName(String name) {
        return new HashSet<>(breedRepository.findByName(name));
    }

    @Override
    public Set<Breed> getAll() {
        Set<Breed> breeds = new HashSet<>();
        breedRepository.findAll().forEach(breeds::add);
        return breeds;
    }

    @Override
    public Breed getById(Long id) {
        // todo : throw NotFoundException here
        return breedRepository.findById(id).orElseThrow(NullPointerException::new);
    }

    @Override
    public void delete(Breed breed) {
        breedRepository.delete(breed);
    }

    @Override
    public void deleteById(Long id) {
        breedRepository.deleteById(id);
    }

    @Override
    public Breed create(Breed breed) {
        // todo : check name for uniqueness
        return breedRepository.save(breed);
    }
}
