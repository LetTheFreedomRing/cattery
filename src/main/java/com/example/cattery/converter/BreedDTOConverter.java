package com.example.cattery.converter;

import com.example.cattery.dto.BreedDTO;
import com.example.cattery.model.Breed;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BreedDTOConverter implements Converter<BreedDTO, Breed> {

    @Override
    public Breed convert(BreedDTO breedDTO) {
        if (breedDTO == null) return null;
        final Breed breed = new Breed();
        breed.setId(breedDTO.getId());
        breed.setCare(breedDTO.getCare());
        breed.setTemper(breedDTO.getTemper());
        breed.setHistory(breedDTO.getHistory());
        breed.setName(breedDTO.getName());
        breed.setOverview(breedDTO.getOverview());
        breed.setImage(breedDTO.getImage());
        return breed;
    }
}
