package com.example.bookstore.service.implementation;

import com.example.bookstore.service.dto.CartDTO;
import com.example.bookstore.service.mapper.CartMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Cart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartRepository;
import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    /**
     * Saves a cart to the database
     * @param cartDTO The cart entity to be saved
     * @return The saved cart entity
     */
    @Override
    public CartDTO creatCart(CartDTO cartDTO) {
        User user = userRepository.findById(cartDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + cartDTO.getUserId()));
        return CartMapper.toCartDTO(cartRepository.save(CartMapper.toCartEntity(cartDTO,user)));
    }

    /**
     * Add a book to an existing cart
     * @param cartId Cart id
     * @param bookId Book id to add
     * @param quantity Number of copies of the book
     * @return The updated cart entity
     */
    @Override
    public CartDTO addABookToCart(Long cartId, Long bookId, int quantity) {

        validateQuantity(quantity);

        Cart cart = findCartById(cartId);
        Book book = findBookById(bookId);

        updateCartWithBook(cart, bookId, quantity);

        return CartMapper.toCartDTO(cartRepository.save(cart));
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    private Cart findCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));
    }

    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
    }

    private void updateCartWithBook(Cart cart, Long bookId, int quantity) {
        Map<Long, Integer> books = cart.getBooks();

        if (books == null) {
            books = new HashMap<>();
            cart.setBooks(books);
        }

        books.put(bookId, books.getOrDefault(bookId, 0) + quantity);
    }

    /**
     * remove books from cart
     * @param cartId The id of the cart
     * @param bookId The id of the book to remove
     */
    @Override
    public void removeBook(Long cartId, Long bookId) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);

        if (cartOptional.isEmpty() ) {
            throw new RuntimeException("Cart not found");
        }

        Cart cart = cartOptional.get();

        Map<Long, Integer> books = cart.getBooks();

        if(books == null || !books.containsKey(bookId)) {
            throw new RuntimeException("Book not found in the cart");
        }

        books.remove(bookId);

        cart.setBooks(books);
        cartRepository.save(cart);

    }

    /**
     * delete a cart
     * @param cartId Cart id to delete
     */
    @Override
    public void deleteCart(Long cartId) {
        if (cartRepository.existsById(cartId)) {
            cartRepository.deleteById(cartId);
        } else {
            throw new RuntimeException("Book not found with id: " + cartId);
        }
    }

    /**
     * Displays the details of a cart
     * @param cartId Cart id of the cart to display
     * @return An Optional containing the cart if found
     */
    @Override
    public Optional<CartDTO> displayCart(Long cartId) {
        return cartRepository.findById(cartId)
                .map(CartMapper::toCartDTO);

    }

    /**
     * View books in the cart
     * @param cartId Cart id
     * @return A map of book IDs and their quantities belongs to the cart
     */
    @Override
    public Map<Long, Integer> viewBooksInCart(Long cartId) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);

        Cart cart = cartOptional.orElseThrow(() -> new RuntimeException("Cart not found"));

        return cart.getBooks() == null ? new HashMap<>() : cart.getBooks();

    }

    /**
     * View current total price
     * @param cartId Cart id of the cart to view the current total price
     * @return Current total price
     */
    @Override
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

    /**
     * View all carts and there items
     * @return A list of all carts
     */
    @Override
    public List<CartDTO> getAllCarts() {
        return cartRepository.findAll()
                .stream()
                .map(CartMapper::toCartDTO)
                .collect(Collectors.toList());

    }

}
