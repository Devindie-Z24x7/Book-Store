package com.example.bookstore.service;

import com.example.bookstore.model.Cart;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.CartRepository;
import com.example.bookstore.repository.OrderRepository;
import com.example.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private final OrderService orderService;
    private final CartRepository cartRepository;

    public UserService(OrderService orderService, CartRepository cartRepository) {
        this.orderService = orderService;
        this.cartRepository = cartRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("Book not found with id: " + id);
        }
    }

    // Method for user to confirm and convert cart to order
    public Optional<Order> confirmAndConvertCartToOrder(Long userId, Long cartId) {
        // Fetch the user (optional validation)
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        // Fetch the cart for this user
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isEmpty() || !cartOptional.get().getUser().equals(userOptional.get())) {
            throw new RuntimeException("Cart not found for the user with id: " + userId);
        }

        // If cart is valid, proceed to convert it to an order by calling OrderService
        return orderService.convertCartToOrder(userId, cartId);
    }

}
