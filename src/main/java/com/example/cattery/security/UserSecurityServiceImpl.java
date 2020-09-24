package com.example.cattery.security;

import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.exceptions.TokenExpiredException;
import com.example.cattery.model.PasswordResetToken;
import com.example.cattery.model.User;
import com.example.cattery.repository.PasswordResetTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@Slf4j
public class UserSecurityServiceImpl implements UserSecurityService {


    private final PasswordResetTokenRepository passwordTokenRepository;

    public UserSecurityServiceImpl(PasswordResetTokenRepository passwordTokenRepository) {
        this.passwordTokenRepository = passwordTokenRepository;
    }

    @Override
    public void createPasswordResetToken(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    @Override
    public void validatePasswordResetToken(String token) throws NotFoundException, TokenExpiredException {
        Optional<PasswordResetToken> optToken = passwordTokenRepository.findByToken(token);
        if (!optToken.isPresent()) {
            throw new NotFoundException();
        } else {
            if (isTokenExpired(optToken.get())) {
                throw new TokenExpiredException();
            }
        }
    }

    @Override
    public User getUserByPasswordResetToken(String token) {
        PasswordResetToken passToken = passwordTokenRepository.findByToken(token).orElseThrow(
                () -> new NotFoundException("Password reset token : " + token + " not found"));
        return passToken.getUser();
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
}
