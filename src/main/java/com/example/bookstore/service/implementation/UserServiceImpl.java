package com.example.bookstore.service.implementation;

import com.example.bookstore.service.dto.OrderDTO;
import com.example.bookstore.service.dto.UserCreateDTO;
import com.example.bookstore.service.dto.UserDTO;
import com.example.bookstore.service.mapper.OrderMapper;
import com.example.bookstore.service.mapper.UserMapper;
import com.example.bookstore.model.Cart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.CartRepository;
import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    private final OrderServiceImpl orderService;
    private final CartRepository cartRepository;

    public UserServiceImpl(OrderServiceImpl orderService, CartRepository cartRepository) {
        this.orderService = orderService;
        this.cartRepository = cartRepository;
    }

    /**
     * Save a user in the repository
     * @param user User to be saved
     * @return Saved user
     */
    @Override
    public UserDTO createUser(UserCreateDTO user) {
        return UserMapper.toUserDTO(userRepository.save(UserMapper.toUserEntity(user)));
    }

    /**
     * Get the user by id
     * @param id User id
     * @return User with the particular id
     */
    @Override
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toUserDTO);
    }

    /**
     * Get all users
     * @return List of users
     */
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    /**
     * Delete a user
     * @param id User id
     */
    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("Book not found with id: " + id);
        }
    }

    /**
     * Method for user to confirm and convert cart to order
     * @param userId User id
     * @param cartId Cart id
     * @return Order created from user's cart
     */
    @Override
    public Optional<OrderDTO> confirmAndConvertCartToOrder(Long userId, Long cartId) {
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
        return orderService.convertCartToOrder(userId, cartId)
                .map(OrderMapper::toOrderDTO);
    }

}
