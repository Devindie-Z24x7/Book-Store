package com.example.bookstore.service;

import com.example.bookstore.service.dto.CartDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CartService {

    CartDTO creatCart(CartDTO cartDto);

    CartDTO addABookToCart(Long cartId, Long bookId, int quantity);

    void removeBook(Long cartId, Long bookId);

    void deleteCart(Long cartId);

    Optional<CartDTO> displayCart(Long cartId);

    Map<Long, Integer> viewBooksInCart(Long cartId);

    Double calculateTotalPrice(Long cartId);

    List<CartDTO> getAllCarts() ;
}
