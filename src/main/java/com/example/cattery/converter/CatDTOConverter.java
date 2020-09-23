package com.example.cattery.converter;

import com.example.cattery.dto.CatDTO;
import com.example.cattery.model.Cat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CatDTOConverter implements Converter<CatDTO, Cat> {

    private final BreedDTOConverter breedDTOConverter;

    public CatDTOConverter(BreedDTOConverter breedDTOConverter) {
        this.breedDTOConverter = breedDTOConverter;
    }

    @Override
    public Cat convert(CatDTO catDTO) {
        if (catDTO == null) return null;
        Cat cat = new Cat();
        cat.setBreed(breedDTOConverter.convert(catDTO.getBreed()));
        cat.setName(catDTO.getName());
        cat.setId(catDTO.getId());
        cat.setBirthDate(catDTO.getBirthDate());
        cat.setCatClass(catDTO.getCatClass());
        cat.setStatus(catDTO.getStatus());
        cat.setPrice(catDTO.getPrice());
        cat.setEms(catDTO.getEms());
        cat.setGender(catDTO.getGender());
        cat.setImages(catDTO.getImages());
        cat.setColour(catDTO.getColour());
        return cat;
    }
}
