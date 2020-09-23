package com.example.cattery.converter;

import com.example.cattery.dto.BreedDTO;
import com.example.cattery.model.Breed;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BreedConverter implements Converter<Breed, BreedDTO> {

    @Override
    public BreedDTO convert(Breed breed) {
        if (breed == null) return null;
        final BreedDTO breedDTO = new BreedDTO();
        breedDTO.setId(breed.getId());
        breedDTO.setCare(breed.getCare());
        breedDTO.setTemper(breed.getTemper());
        breedDTO.setHistory(breed.getHistory());
        breedDTO.setName(breed.getName());
        breedDTO.setOverview(breed.getOverview());
        breedDTO.setImage(breed.getImage());
        return breedDTO;
    }
}
