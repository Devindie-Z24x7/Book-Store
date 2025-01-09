package com.example.bookstore.controller;

import com.example.bookstore.service.UserService;
import com.example.bookstore.service.dto.OrderDTO;
import com.example.bookstore.service.dto.UserCreateDTO;
import com.example.bookstore.service.dto.UserDTO;
//import com.example.bookstore.service.implementation.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for users
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Creates a new user.
     *
     * @param user the UserCreateDTO object containing the user's details.
     * @return a ResponseEntity containing the created UserDTO object.
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO user) {

        log.info("createUser() - Request received to create user with details: {}", user);

        UserDTO createdUser = userService.createUser(user);

        log.info("createUser() - User created successfully with ID: {}", createdUser.getId());

        return ResponseEntity.ok(createdUser);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve.
     * @return a ResponseEntity containing the UserDTO if found, or a 404 status if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {

        log.info("getUserById() - Request received for User ID: {}", id);

        return userService.getUserById(id)
                .map(userDTO -> {
                    log.info("getUserById() - User found. User ID: {}", id);
                    return ResponseEntity.ok(userDTO);
                })
                .orElseGet(() -> {
                    log.warn("getUserById() - User not found. User ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Retrieves all users.
     *
     * @return a ResponseEntity containing a list of all UserDTO objects.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        log.info("getAllUsers() - Request received to retrieve all users.");

        List<UserDTO> users = userService.getAllUsers();

        log.info("getAllUsers() - Retrieved {} users.", users.size());

        return ResponseEntity.ok(users);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete.
     * @return a ResponseEntity with a 204 status if the user is successfully deleted, or a 404 status if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("deleteUser() - Request received to delete User ID: {}", id);

        userService.deleteUser(id);

        log.info("deleteUser() - User ID: {} has been successfully deleted.", id);

        return ResponseEntity.noContent().build();

    }


    /**
     * Converts a cart to an order for a specific user.
     *
     * @param userId the ID of the user.
     * @param cartId the ID of the cart to be converted.
     * @return a ResponseEntity containing the created OrderDTO if successful, or a 400 status if the operation fails.
     */
    @PostMapping("/{userId}/{cartId}/confirm")
    public ResponseEntity<OrderDTO> convertCartToOrder(@PathVariable Long userId, @PathVariable Long cartId) {

        log.info("convertCartToOrder() - Request received. User ID: {}, Cart ID: {}", userId, cartId);

        return userService.confirmAndConvertCartToOrder(userId, cartId)
                .map(orderDTO -> {
                    log.info("convertCartToOrder() - Cart successfully converted to order. User ID: {}, Cart ID: {}", userId, cartId);
                    return ResponseEntity.ok(orderDTO);
                })
                .orElseGet(() -> {
                    log.warn("convertCartToOrder() - Failed to convert Cart to Order. User ID: {}, Cart ID: {}", userId, cartId);
                    return ResponseEntity.badRequest().build();
                });
    }

}
