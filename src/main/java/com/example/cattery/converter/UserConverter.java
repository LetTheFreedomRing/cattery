package com.example.cattery.converter;

import com.example.cattery.dto.UserDTO;
import com.example.cattery.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<User, UserDTO> {

    @Override
    public UserDTO convert(User user) {
        if (user == null) return null;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setMatchingPassword(user.getPassword());
        userDTO.setName(user.getName());
        return userDTO;
    }
}
