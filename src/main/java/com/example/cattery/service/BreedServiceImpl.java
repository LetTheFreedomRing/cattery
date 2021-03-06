package com.example.cattery.service;

import com.example.cattery.converter.BreedConverter;
import com.example.cattery.converter.BreedDTOConverter;
import com.example.cattery.dto.BreedDTO;
import com.example.cattery.exceptions.BreedAlreadyExistException;
import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.model.Breed;
import com.example.cattery.repository.BreedRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BreedServiceImpl implements BreedService {

    private final BreedRepository breedRepository;

    private final BreedImageService breedImageService;

    private final BreedConverter breedConverter;

    private final BreedDTOConverter breedDTOConverter;

    public BreedServiceImpl(BreedRepository breedRepository, BreedImageService breedImageService,
                            BreedConverter breedConverter, BreedDTOConverter breedDTOConverter) {
        this.breedRepository = breedRepository;
        this.breedImageService = breedImageService;
        this.breedConverter = breedConverter;
        this.breedDTOConverter = breedDTOConverter;
    }

    @Override
    public Breed getByName(String name) {
        return breedRepository.findByName(name).orElseThrow(
                () -> new NotFoundException("Breed with name : " + name + " not found"));
    }

    @Override
    public Set<Breed> getAll() {
        Set<Breed> breeds = new HashSet<>();
        breedRepository.findAll().forEach(breeds::add);
        return breeds;
    }

    @Override
    public Set<BreedDTO> getAllDTOs() {
        return getAll().stream().map(breedConverter::convert).collect(Collectors.toSet());
    }

    @Override
    public Breed getById(Long id) {
        return breedRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Breed with id : " + id + " not found"));
    }

    @Override
    public BreedDTO getDTOById(Long id) {
        return breedConverter.convert(getById(id));
    }

    @Override
    public void deleteById(Long id) {
        breedRepository.deleteById(id);
    }

    @Override
    public Breed create(BreedDTO breedDTO) throws BreedAlreadyExistException {
        if ((isNew(breedDTO) && breedExists(breedDTO.getName())) ||
                (breedExists(breedDTO.getName()) && !isSameId(breedDTO, breedDTO.getName()))) {
            throw new BreedAlreadyExistException();
        } else {
            if (breedDTO.getId() == null && breedDTO.getImage() == null) {
                breedDTO.setImage(breedImageService.getDefaultImageBytes());
            } else if (breedDTO.getId() != null && breedDTO.getImage() == null) {
                breedDTO.setImage(breedRepository.findById(breedDTO.getId()).get().getImage());
            }
            return breedRepository.save(breedDTOConverter.convert(breedDTO));
        }
    }

    private boolean breedExists(String name) {
        return breedRepository.findByName(name).isPresent();
    }

    private boolean isNew(BreedDTO breedDTO) {
        return breedDTO.getId() == null;
    }

    private boolean isSameId(BreedDTO breedDTO, String name) {
        return breedRepository.findByName(name).orElseThrow(RuntimeException::new).getId().equals(breedDTO.getId());
    }
}
