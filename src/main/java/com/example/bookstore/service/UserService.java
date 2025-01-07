package com.example.bookstore.service;

import com.example.bookstore.service.dto.OrderDTO;
import com.example.bookstore.service.dto.UserCreateDTO;
import com.example.bookstore.service.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDTO createUser(UserCreateDTO user) ;

    Optional<UserDTO> getUserById(Long id);

    List<UserDTO> getAllUsers();

    void deleteUser(Long id);

    Optional<OrderDTO> confirmAndConvertCartToOrder(Long userId, Long cartId);
}
