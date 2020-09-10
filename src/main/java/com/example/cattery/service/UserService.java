package com.example.cattery.service;

import com.example.cattery.dto.UserDTO;
import com.example.cattery.exceptions.UserAlreadyExistException;
import com.example.cattery.model.User;
import com.example.cattery.model.VerificationToken;

import java.util.Set;

public interface UserService {

    Set<User> getByName(String name);

    User getById(Long id);

    User registerNewAccount(UserDTO userDTO) throws UserAlreadyExistException;

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String verificationToken);

    void deleteById(Long id);
}
