package com.example.cattery.repository;

import com.example.cattery.model.Cat;
import com.example.cattery.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class UserRepositoryIT {
    private static final String USER_NAME = "NAME";
    private static final String USER_EMAIL = "bla@mail.com";
    private static final String USER_PASSWORD = "password";
    private static final String USER_MATCHING_PASSWORD = "password";
    private static final LocalDate USER_REGISTRATION_DATE = LocalDate.now();

    @Autowired
    private UserRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    private static User prepareUser() {
        User user = new User();
        user.setName(USER_NAME);
        user.setMatchingPassword(USER_MATCHING_PASSWORD);
        user.setPassword(USER_PASSWORD);
        user.setEmail(USER_EMAIL);
        user.setRegistrationDate(USER_REGISTRATION_DATE);
        return user;
    }

    @Test
    void save() {
        // given
        User user = prepareUser();

        // when
        repository.save(user);

        // then
        assertEquals(1, repository.count());

        User foundUser = repository.findAll().iterator().next();

        assertNotNull(foundUser.getId());
        assertEquals(USER_NAME, foundUser.getName());
        assertEquals(USER_REGISTRATION_DATE, foundUser.getRegistrationDate());
    }

    @Test
    void findByName() {
        //given
        User savedUser = repository.save(prepareUser());

        // when
        Collection<User> foundUsers = repository.findByName(USER_NAME);

        // then
        assertEquals(1, foundUsers.size());
        assertEquals(savedUser, foundUsers.iterator().next());
    }

    @Test
    void findById() {
        // given
        Long savedId = repository.save(prepareUser()).getId();

        // when
        User foundUser = repository.findById(savedId).orElseThrow(NullPointerException::new);

        // then
        assertEquals(USER_NAME, foundUser.getName());
    }

    @Test
    void delete() {
        // given
        User savedUser = repository.save(prepareUser());

        // when
        repository.delete(savedUser);

        // then
        assertEquals(0, repository.count());
    }

    @Test
    void deleteById() {
        // given
        Long savedId = repository.save(prepareUser()).getId();

        // when
        repository.deleteById(savedId);

        // then
        assertEquals(0, repository.count());
    }

    @Test
    void catsTest() {
        // given
        User user = prepareUser();

        Cat cat1 = new Cat();
        cat1.setName("cat1");

        Cat cat2 = new Cat();
        cat2.setName("cat2");

        user.getCats().add(cat1);
        user.getCats().add(cat2);

        // when
        Long userId = repository.save(user).getId();

        // then
        User foundUser = repository.findById(userId).orElseThrow(NullPointerException::new);

        assertEquals(2, foundUser.getCats().size());
    }

    @Test
    void wishlistTest() {
        // given
        User user = prepareUser();

        Cat cat1 = new Cat();
        cat1.setName("cat1");

        Cat cat2 = new Cat();
        cat2.setName("cat2");

        user.getWishList().add(cat1);
        user.getWishList().add(cat2);

        // when
        Long userId = repository.save(user).getId();

        // then
        User foundUser = repository.findById(userId).orElseThrow(NullPointerException::new);

        assertEquals(2, foundUser.getWishList().size());
    }
}