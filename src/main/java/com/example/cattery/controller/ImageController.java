package com.example.cattery.controller;

import com.example.cattery.model.Breed;
import com.example.cattery.model.Cat;
import com.example.cattery.repository.BreedRepository;
import com.example.cattery.repository.CatRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@Slf4j
public class ImageController {

    private final CatRepository catRepository;
    private final BreedRepository breedRepository;

    public ImageController(CatRepository catRepository, BreedRepository breedRepository) {
        this.catRepository = catRepository;
        this.breedRepository = breedRepository;
    }

    @GetMapping("cat/{catId}/catimage")
    public void renderCatImageFromDb(@PathVariable(name = "catId") Long catId, HttpServletResponse response) {
        // todo : throw NotFoundException
        Cat cat = catRepository.findById(catId).orElseThrow(NullPointerException::new);

        if (cat.getImages().size() != 0) {

            Byte[] image = cat.getImages().get(0);
            byte[] bytes = new byte[image.length];

            int i = 0;
            for (Byte b : image) {
                bytes[i++] = b;
            }

            response.setContentType("image/jpeg");
            try (InputStream is = new ByteArrayInputStream(bytes)) {
                IOUtils.copy(is, response.getOutputStream());
            } catch (IOException ex) {
                log.error("Error returning image", ex);
            }
        }
    }

    @GetMapping("breed/{breedId}/breedimage")
    public void renderBreedImageFromDb(@PathVariable(name = "breedId") Long breedId, HttpServletResponse response) {
        // todo : throw NotFoundException
        Breed breed = breedRepository.findById(breedId).orElseThrow(NullPointerException::new);

        byte[] bytes = new byte[breed.getImage().length];

        int i = 0;
        for (Byte b : breed.getImage()) {
            bytes[i++] = b;
        }

        response.setContentType("image/jpeg");
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            IOUtils.copy(is, response.getOutputStream());
        } catch (IOException ex) {
            log.error("Error returning image", ex);
        }
    }

}
