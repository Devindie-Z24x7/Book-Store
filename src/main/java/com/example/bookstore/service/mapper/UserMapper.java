package com.example.bookstore.service.mapper;

import com.example.bookstore.service.dto.UserCreateDTO;
import com.example.bookstore.service.dto.UserDTO;
import com.example.bookstore.model.User;

public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }

    public static User toUserEntity(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setId(userCreateDTO.getId());
        user.setUsername(userCreateDTO.getUsername());
        user.setPassword(userCreateDTO.getPassword()); // Ensure password is properly set
        return user;
    }

}
