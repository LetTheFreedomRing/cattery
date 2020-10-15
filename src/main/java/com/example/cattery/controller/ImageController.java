package com.example.cattery.controller;

import com.example.cattery.model.Breed;
import com.example.cattery.model.Cat;
import com.example.cattery.service.BreedService;
import com.example.cattery.service.CatService;
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

    private final CatService catService;
    private final BreedService breedService;

    public ImageController(CatService catService, BreedService breedService) {
        this.catService = catService;
        this.breedService = breedService;
    }

    @GetMapping("cat/{catId}/catimage/{imageNum}")
    public void renderCatImageFromDb(@PathVariable(name = "catId") Long catId,
                                     @PathVariable(name = "imageNum") Integer imageNum,
                                     HttpServletResponse response) {
        Cat cat = catService.getById(catId);

        if (cat.getImages().size() != 0 && cat.getImages().size() > imageNum) {

            Byte[] image = cat.getImages().get(imageNum);
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
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    @GetMapping("breed/{breedId}/breedimage")
    public void renderBreedImageFromDb(@PathVariable(name = "breedId") Long breedId, HttpServletResponse response) {
        Breed breed = breedService.getById(breedId);

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
            throw new RuntimeException(ex.getMessage());
        }
    }

}
