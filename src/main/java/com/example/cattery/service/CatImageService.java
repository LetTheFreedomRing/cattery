package com.example.cattery.service;

import com.example.cattery.model.Cat;
import com.example.cattery.repository.CatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class CatImageService extends ImageService {

    @Value("classpath:static/images/default_cat.jpg")
    private Resource defaultImage;

    private final CatRepository catRepository;

    public CatImageService(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    @Override
    public void saveImage(Long catId, MultipartFile image) {
        try {
            Optional<Cat> optionalCat = catRepository.findById(catId);
            if (!optionalCat.isPresent()) {
                throw new RuntimeException("Cat with id " + catId + " not found");
            }
            Cat cat = optionalCat.get();

            byte[] imageBytes = image.getBytes();
            Byte[] bytes = new Byte[imageBytes.length];
            for (int i = 0; i < bytes.length; bytes[i] = imageBytes[i], i++);
            cat.getImages().add(bytes);
            catRepository.save(cat);
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
