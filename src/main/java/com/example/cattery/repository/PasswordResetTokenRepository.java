package com.example.cattery.repository;

import com.example.cattery.model.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);
}
