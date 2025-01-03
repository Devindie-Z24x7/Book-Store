package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Cart;
import com.example.bookstore.model.Order;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartRepository;
import com.example.bookstore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CartService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;

    @Autowired
    public CartService(OrderRepository orderRepository, CartRepository cartRepository, BookRepository bookRepository) {
        this.orderRepository = orderRepository;
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

        if(books == null || !books.containsKey(bookId)){
            throw new RuntimeException("Book not found in the cart");
        }

        books.remove(bookId);

        cart.setBooks(books);
        cartRepository.save(cart);

    }

    //delete a cart
    public void deleteBook(Long cartId){
        if (cartRepository.existsById(cartId)) {
            cartRepository.deleteById(cartId);
        } else {
            throw new RuntimeException("Book not found with id: " + cartId);
        }
    }

    //view the cart
    public Optional<Cart> displayCart(Long cartId){
        return cartRepository.findById(cartId);

    }

    //view books in the cart
    public Map<Long, Integer> viewBooksInCart(Long cartId){
        Optional<Cart> cartOptional = cartRepository.findById(cartId);

        Cart cart = cartOptional.orElseThrow(() -> new RuntimeException("Cart not found"));

        return cart.getBooks() == null ? new HashMap<>() : cart.getBooks();

    }

    //view current total price
    public Double calculateTotalPrice(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Map<Long, Integer> books = cart.getBooks();

        if (books == null || books.isEmpty()) {
            return 0.0;
        }

        return books.entrySet().stream()
                .mapToDouble(entry -> bookRepository.findById(entry.getKey())
                        .map(book -> book.getPrice() * entry.getValue())
                        .orElse(0.0))
                .sum();
    }

    //view all carts and there items
    public List<Cart> getAllCarts(){
        return cartRepository.findAll();

    }

    // Convert a cart to an order upon user confirmation
    public Order convertCartToOrder(Long cartId, String orderStatus) {
        // Fetch the cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Use CartService to calculate the total price
        Double total = this.calculateTotalPrice(cartId);

        // Create the Order object
        Order order = new Order(cart, total, LocalDateTime.now(), orderStatus);

        // Save the Order
        order = orderRepository.save(order);

        // Remove the cart after creating the Order
        cartRepository.delete(cart);

        return order;
    }


}
