package com.example.cattery.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public abstract class ImageService {

    public abstract void saveImage(Long objId, MultipartFile image);

    public abstract Byte[] getDefaultImageBytes();

    protected Byte[] getDefaultImageBytes(Resource image) {
        try {
            File imageFile =  image.getFile();
            log.debug("File exists : " + imageFile.exists());
            byte[] bytes = Files.readAllBytes(imageFile.toPath());
            Byte[] res = new Byte[bytes.length];
            int i = 0;
            for (byte b : bytes) {
                res[i++] = b;
            }
            return res;

        } catch (IOException e) {
            log.error("getDefaultImage() error : " + e.getMessage());
        }
        return null;
    }

}
