package com.example.cattery.service;

import com.example.cattery.converter.CatConverter;
import com.example.cattery.converter.CatDTOConverter;
import com.example.cattery.dto.CatDTO;
import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.model.Cat;
import com.example.cattery.model.CatStatus;
import com.example.cattery.model.User;
import com.example.cattery.repository.BreedRepository;
import com.example.cattery.repository.CatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class CatServiceImpl implements CatService {

    private final CatRepository catRepository;

    private final CatImageService catImageService;

    private final CatConverter catConverter;

    private final CatDTOConverter catDTOConverter;

    private final BreedRepository breedRepository;

    public CatServiceImpl(CatRepository catRepository, BreedRepository breedRepository, CatImageService catImageService,
                          CatConverter catConverter, CatDTOConverter catDTOConverter) {
        this.catRepository = catRepository;
        this.catImageService = catImageService;
        this.catConverter = catConverter;
        this.catDTOConverter = catDTOConverter;
        this.breedRepository = breedRepository;
    }

    @Override
    public Cat getById(Long id) {
        return catRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Cat with id : " + id + " not found"));
    }

    @Override
    public CatDTO getDTOById(Long id) {
        return catConverter.convert(getById(id));
    }

    @Override
    public void deleteById(Long id) {
        catRepository.deleteById(id);
    }

    @Override
    public void updateOwner(User user, Cat cat) {
        user.getCats().add(cat);
        cat.setOwner(user);
        cat.setStatus(CatStatus.SOLD);
        catRepository.save(cat);
    }

    @Override
    public Cat create(CatDTO catDTO) {

        if (!breedExists(catDTO.getBreed().getId())) {
            log.debug("Breed with id : " + catDTO.getBreed().getId() + " not found");
            throw new NotFoundException("Breed with id : " + catDTO.getBreed().getId() + " not found");
        }

        if (catDTO.getImages().size() == 0) {
            Byte[] image = catImageService.getDefaultImageBytes();
            catDTO.getImages().add(image);
        }
        Cat cat = catDTOConverter.convert(catDTO);
        cat.setLastUpdated(LocalDate.now());
        return catRepository.save(cat);
    }

    private boolean breedExists(Long id) {
        if (id == null) return false;
        return breedRepository.findById(id).isPresent();
    }
}
