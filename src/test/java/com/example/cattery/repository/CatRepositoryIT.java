package com.example.cattery.repository;

import com.example.cattery.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class CatRepositoryIT {

    private static final String CAT_NAME = "Cat";
    private static final String CAT_COLOUR = "Colour";
    private static final String CAT_EMS = "EMS";
    private static final LocalDate CAT_BIRTH_DATE = LocalDate.now();
    private static final Gender CAT_GENDER = Gender.MALE;
    private static final CatStatus CAT_STATUS = CatStatus.AVAILABLE;
    private static final CatClass CAT_CLASS = CatClass.PET;

    @Autowired
    private CatRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    private static Cat prepareCat() {
        Cat cat = new Cat();
        cat.setName(CAT_NAME);
        cat.setBirthDate(CAT_BIRTH_DATE);
        cat.setGender(CAT_GENDER);
        cat.setColour(CAT_COLOUR);
        cat.setEms(CAT_EMS);
        cat.setStatus(CAT_STATUS);
        cat.setCatClass(CAT_CLASS);
        return cat;
    }

    @Test
    void save() {
        // given
        Cat cat = prepareCat();

        // when
        repository.save(cat);

        // then
        assertEquals(1L, repository.count());

        Cat foundCat = repository.findAll().iterator().next();

        assertNotNull(foundCat.getId());
        assertEquals(CAT_NAME, foundCat.getName());
        assertEquals(CAT_BIRTH_DATE, foundCat.getBirthDate());
        assertEquals(CAT_COLOUR, foundCat.getColour());
        assertEquals(CAT_CLASS, foundCat.getCatClass());
        assertEquals(CAT_STATUS, foundCat.getStatus());
        assertEquals(CAT_EMS, foundCat.getEms());
        assertEquals(CAT_GENDER, foundCat.getGender());
    }

    @Test
    void breedIsStoredCorrectly(@Autowired BreedRepository breedRepository) {
        // given
        Cat cat = prepareCat();
        Breed breed = new Breed();
        cat.setBreed(breed);

        // when
        Breed savedBreed = breedRepository.save(breed);
        Cat savedCat = repository.save(cat);

        // then
        assertEquals(1, breedRepository.count());
        assertEquals(1, repository.count());

        Breed foundBreed = repository.findById(savedCat.getId()).orElseThrow(NullPointerException::new).getBreed();

        assertEquals(foundBreed.getId(), savedBreed.getId());
    }

    @Test
    void findById() {
        // given
        Long savedId = repository.save(prepareCat()).getId();

        // when
        Cat foundCat = repository.findById(savedId).orElseThrow(NullPointerException::new);

        // then
        assertEquals(CAT_NAME, foundCat.getName());
    }

    @Test
    void delete() {
        // given
        Cat savedCat = repository.save(prepareCat());

        // when
        repository.delete(savedCat);

        // then
        assertEquals(0, repository.count());
    }

    @Test
    void deleteById() {
        // given
        Long savedId = repository.save(prepareCat()).getId();

        // when
        repository.deleteById(savedId);

        // then
        assertEquals(0, repository.count());
    }

}