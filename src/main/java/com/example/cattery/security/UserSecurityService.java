package com.example.cattery.security;

import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.exceptions.TokenExpiredException;
import com.example.cattery.model.User;

public interface UserSecurityService {

    void createPasswordResetToken(User user, String token);

    void validatePasswordResetToken(String token) throws NotFoundException, TokenExpiredException;

    User getUserByPasswordResetToken(String token);
}
