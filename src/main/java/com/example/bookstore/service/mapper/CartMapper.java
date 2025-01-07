package com.example.bookstore.service.mapper;

import com.example.bookstore.service.dto.CartDTO;
import com.example.bookstore.model.Cart;
import com.example.bookstore.model.User;

public class CartMapper {

    public static CartDTO toCartDTO(Cart cart) {
        return CartDTO.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getId())
                .books(cart.getBooks())
                .build();
    }

    public static Cart toCartEntity(CartDTO cartDTO, User user) {
        Cart cart = new Cart();
        cart.setCartId(cartDTO.getCartId());
        cart.setUser(user);
        cart.setBooks(cartDTO.getBooks());
        return cart;
    }


}
