package com.example.cattery.service;

import com.example.cattery.converter.UserConverter;
import com.example.cattery.converter.UserDTOConverter;
import com.example.cattery.dto.UserDTO;
import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.exceptions.UserAlreadyExistException;
import com.example.cattery.model.User;
import com.example.cattery.repository.RoleRepository;
import com.example.cattery.repository.UserRepository;
import com.example.cattery.model.VerificationToken;
import com.example.cattery.repository.VerificationTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final VerificationTokenRepository tokenRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserConverter userConverter;

    private final UserDTOConverter userDTOConverter;

    public UserServiceImpl(UserRepository userRepository, VerificationTokenRepository tokenRepository,
                           RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                           UserConverter userConverter, UserDTOConverter userDTOConverter) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userConverter = userConverter;
        this.userDTOConverter = userDTOConverter;
    }

    @Override
    public Set<User> getByName(String name) {
        return new HashSet<>(userRepository.findByName(name));
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getDTOByEmail(String email) {
        return userConverter.convert(userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email : " + email + " not found")));
    }

    @Override
    @Transactional
    public User registerNewAccount(UserDTO userDTO) throws UserAlreadyExistException {
        if (emailExist(userDTO.getEmail())) {
            throw new UserAlreadyExistException(
                    "There is an account with that email address: " +  userDTO.getEmail());
        }
        final User user = userDTOConverter.convert(userDTO);
        // encrypt password and set USER role
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found"))));
        return userRepository.save(user);
    }

    @Override
    public User getUser(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).orElseThrow(NotFoundException::new).getUser();
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken).orElseThrow(NotFoundException::new);
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
