package com.example.cattery.service;

import com.example.cattery.model.Breed;
import com.example.cattery.repository.BreedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class BreedImageService extends ImageService {

    @Value("classpath:static/images/default_breed.jpg")
    private Resource defaultImage;

    private final BreedRepository breedRepository;

    public BreedImageService(BreedRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    @Override
    public void saveImage(Long breedId, MultipartFile image) {
        try {
            Optional<Breed> optionalBreed = breedRepository.findById(breedId);
            if (!optionalBreed.isPresent()) {
                throw new RuntimeException("Breed with id " + breedId + " not found");
            }
            Breed breed = optionalBreed.get();

            byte[] imageBytes = image.getBytes();
            Byte[] bytes = new Byte[imageBytes.length];
            for (int i = 0; i < bytes.length; bytes[i] = imageBytes[i], i++);
            breed.setImage(bytes);
            breedRepository.save(breed);
        } catch (IOException e) {
            //todo: handle better
            log.error("Error occurred", e);
            e.printStackTrace();
        }
    }

    @Override
    public Byte[] getDefaultImageBytes() {
        return getImageBytes(defaultImage);
    }
}
