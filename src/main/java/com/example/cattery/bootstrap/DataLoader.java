package com.example.cattery.bootstrap;

import com.example.cattery.model.*;
import com.example.cattery.repository.BreedRepository;
import com.example.cattery.repository.CatRepository;
import com.example.cattery.repository.CommentRepository;
import com.example.cattery.repository.UserRepository;
import com.example.cattery.service.BreedImageService;
import com.example.cattery.service.CatImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final CatRepository catRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BreedRepository breedRepository;
    private final CatImageService catImageService;
    private final BreedImageService breedImageService;

    public DataLoader(CatRepository catRepository, UserRepository userRepository,
                      CommentRepository commentRepository, BreedRepository breedRepository,
                      CatImageService catImageService, BreedImageService breedImageService) {
        this.catRepository = catRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.breedRepository = breedRepository;
        this.catImageService = catImageService;
        this.breedImageService = breedImageService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadData();
        log.debug("Data loaded");
        log.debug("Users count : " + userRepository.count());
        log.debug("Cats count : " + catRepository.count());
        log.debug("Comments count : " + commentRepository.count());
    }

    private void loadData() {
        // create breeds
        Breed british = new Breed();
        british.setName("British shorthair");
        british.setDescription("The British Shorthair is solid and muscular with an easygoing personality. As befits his British heritage, he is slightly reserved, but once he gets to know someone he’s quite affectionate. His short, dense coat comes in many colors and patterns and should be brushed two or three times a week to remove dead hair");
        british.setImage(breedImageService.getDefaultImageBytes());

        // create users
        User user1 = new User();
        user1.setName("John");
        user1.setRegistrationDate(LocalDate.now());

        User user2 = new User();
        user2.setName("Mary");
        user2.setRegistrationDate(LocalDate.now());

        // create cats
        Cat cat1 = new Cat();
        cat1.setName("Mimiko");
        cat1.setBreed(british);
        cat1.setCatClass(CatClass.BREEDING);
        british.getCats().add(cat1);
        cat1.setStatus(CatStatus.SOLD);
        cat1.setColour("black golden spotted");
        cat1.setEms("BRI ny 24");
        cat1.setGender(Gender.MALE);
        cat1.setPrice(330);
        cat1.setBirthDate(LocalDate.of(2019, 2, 22));
        cat1.setLastUpdated(LocalDate.now());
        cat1.getImages().add(catImageService.getDefaultImageBytes());

        Cat cat2 = new Cat();
        cat2.setName("Kim");
        cat2.setBreed(british);
        british.getCats().add(cat2);
        cat2.setBirthDate(LocalDate.of(2013, 3, 17));
        cat2.setLastUpdated(LocalDate.now());
        cat2.setGender(Gender.MALE);
        cat2.setStatus(CatStatus.SOLD);
        cat2.setColour("black golden blotched");
        cat2.setEms("BRI ny 22 64");
        cat2.setCatClass(CatClass.BREEDING);
        cat2.setOwner(user2);
        cat2.setPrice(330);
        cat2.getImages().add(catImageService.getDefaultImageBytes());
        user2.getCats().add(cat2);

        // create comments
        Comment comment = new Comment();
        comment.setMessage("How cute!");
        comment.setDate(LocalDate.now());
        comment.setUser(user1);
        comment.setCat(cat1);

        breedRepository.save(british);
        userRepository.save(user1);
        userRepository.save(user2);
        catRepository.save(cat1);
        catRepository.save(cat2);
        commentRepository.save(comment);
    }
}
