package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Cart;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;

    @Autowired
    public CartService(CartRepository cartRepository, BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
    }

    //create a cart
    public Cart creatCart(Cart cart){
        return cartRepository.save(cart);
    }

    //add books to cart
    public Cart addABookToCart(Long cartId, Long bookId, int quantity) {
        // Validate quantity
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (cartOptional.isEmpty() || bookOptional.isEmpty()) {
            throw new RuntimeException("Cart or Book not found");
        }

        Cart cart = cartOptional.get();

        // books map is initialized
        Map<Long, Integer> books = cart.getBooks();

        if (books == null) {
            books = new HashMap<>();
        }

        // Check if the book is already in the cart
        if (books.containsKey(bookId)) {
            books.put(bookId, books.get(bookId) + quantity); // Update quantity
        } else {
            books.put(bookId, quantity); // Add new book
        }

        cart.setBooks(books); // Update the books map
        return cartRepository.save(cart); // Persist the changes
    }

    //remove books from cart
    public void removeBook(Long cartId, Long bookId){
        Optional<Cart> cartOptional = cartRepository.findById(cartId);

        if (cartOptional.isEmpty() ) {
            throw new RuntimeException("Cart not found");
        }

        Cart cart = cartOptional.get();

        // books map is initialized
        Map<Long, Integer> books = cart.getBooks();

        if(books == null || books.containsKey(bookId)){
            throw new RuntimeException("Book not found in the cart");
        }

        books.remove(bookId);

        cart.setBooks(books);
        cartRepository.save(cart);

    }



    //edit the book quantity
    //view items in the cart


}
