package com.example.cattery.service;

import com.example.cattery.exceptions.NotFoundException;
import com.example.cattery.exceptions.UserAlreadyExistException;
import com.example.cattery.model.User;
import com.example.cattery.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Set<User> getByName(String name) {
        return new HashSet<>(userRepository.findByName(name));
    }

    @Override
    public Set<User> getAll() {
        Set<User> users = new HashSet<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User create(User user) throws UserAlreadyExistException {
        if (emailExist(user.getEmail())) {
            throw new UserAlreadyExistException(
                    "There is an account with that email address: " +  user.getEmail());
        }
        return userRepository.save(user);
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
