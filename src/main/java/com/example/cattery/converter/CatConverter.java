package com.example.cattery.converter;

import com.example.cattery.dto.CatDTO;
import com.example.cattery.model.Cat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CatConverter implements Converter<Cat, CatDTO> {

    private final BreedConverter breedConverter;

    public CatConverter(BreedConverter breedConverter) {
        this.breedConverter = breedConverter;
    }

    @Override
    public CatDTO convert(Cat cat) {
        if (cat == null) return null;
        CatDTO catDTO = new CatDTO();
        catDTO.setBreed(breedConverter.convert(cat.getBreed()));
        catDTO.setName(cat.getName());
        catDTO.setId(cat.getId());
        catDTO.setBirthDate(cat.getBirthDate());
        catDTO.setCatClass(cat.getCatClass());
        catDTO.setStatus(cat.getStatus());
        catDTO.setPrice(cat.getPrice());
        catDTO.setEms(cat.getEms());
        catDTO.setGender(cat.getGender());
        catDTO.setImages(cat.getImages());
        catDTO.setColour(cat.getColour());
        return catDTO;
    }
}
