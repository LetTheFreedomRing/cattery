package com.example.cattery.repository;

import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.model.User;
import com.example.cattery.model.VerificationToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class VerificationTokenRepositoryTest {

    private static final String TOKEN = "token";

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @Test
    void findByToken() {
        // given
        User savedUser = userRepository.save(prepareUser());
        VerificationToken token = new VerificationToken(TOKEN);
        token.setUser(savedUser);
        tokenRepository.save(token);

        // when
        VerificationToken foundToken = tokenRepository.findByToken(TOKEN).orElseThrow(NotFoundException::new);

        // then
        assertEquals(TOKEN, foundToken.getToken());
    }

    @Test
    void findByTokenNotFound() {
        // given
        User savedUser = userRepository.save(prepareUser());
        VerificationToken token = new VerificationToken(TOKEN);
        token.setUser(savedUser);
        tokenRepository.save(token);

        // when
        Optional<VerificationToken> foundToken = tokenRepository.findByToken("blabla");

        assertFalse(foundToken.isPresent());
    }

    @Test
    void findByUser() {
        // given
        User savedUser = userRepository.save(prepareUser());
        VerificationToken token = new VerificationToken(TOKEN);
        token.setUser(savedUser);
        tokenRepository.save(token);

        // when
        VerificationToken foundToken = tokenRepository.findByUser(savedUser).orElseThrow(NotFoundException::new);

        // then
        assertEquals(TOKEN, foundToken.getToken());
    }

    @Test
    void findByUserNotFound() {
        // given
        User savedUser = userRepository.save(prepareUser());
        User anotherUser = userRepository.save(new User());
        VerificationToken token = new VerificationToken(TOKEN);
        token.setUser(savedUser);
        tokenRepository.save(token);

        // when
        Optional<VerificationToken> foundToken = tokenRepository.findByUser(anotherUser);

        assertFalse(foundToken.isPresent());
    }

    private User prepareUser() {
        User user = new User();
        user.setName("Name");
        user.setEmail("Email");
        user.setPassword("Password");
        return user;
    }
}