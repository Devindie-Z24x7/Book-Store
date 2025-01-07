package com.example.bookstore.controller;

import com.example.bookstore.service.dto.CartDTO;
import com.example.bookstore.service.implementation.CartServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Cart
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    @Autowired
    private final CartServiceImpl cartService;

    public CartController(CartServiceImpl cartService) {

        this.cartService = cartService;
    }

    /**
     * Retrieves a list of all carts.
     *
     * @return a list of all CartDTO objects.
     */
    @GetMapping("/all")
    public List<CartDTO> displayAllCarts() {

        log.info("displayAllCarts() - request received");

        List<CartDTO> carts = cartService.getAllCarts();

        log.info("getAllBooks() - Response prepared with {} books.", carts.size());
        return carts ;
    }

    /**
     * Retrieves a specific cart by its ID.
     *
     * @param id the ID of the cart to retrieve.
     * @return a ResponseEntity containing the CartDTO if found, or a 404 status if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> displayCart(@PathVariable Long id) {
        log.info("displayCart() - Request received. Cart ID: {}", id);

        return cartService.displayCart(id)
                .map(cartDTO -> {
                    log.info("displayCart() - Cart found. Cart ID: {}", id);
                    return ResponseEntity.ok(cartDTO);
                })
                .orElseGet(() -> {
                    log.warn("displayCart() - Cart not found. Cart ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Retrieves all books in a specific cart.
     *
     * @param id the ID of the cart whose books are to be retrieved.
     * @return a ResponseEntity containing the list of books in the cart.
     */
    @GetMapping("/{id}/books")
    public ResponseEntity<?> viewBooksInCart(@PathVariable Long id) {
        log.info("viewBooksInCart() - Request received. Cart ID: {}", id);

        var books = cartService.viewBooksInCart(id);
        log.info("viewBooksInCart() - Retrieved {} books from Cart ID: {}", books.size(), id);

        return ResponseEntity.ok(books);
    }

    /**
     * Calculates the total price of the books in a specific cart.
     *
     * @param id the ID of the cart whose total price is to be calculated.
     * @return a ResponseEntity containing the total price as a double.
     */
    @GetMapping("/{id}/totalPrice")
    public ResponseEntity<Double> calculateTotalPrice(@PathVariable Long id) {
        log.info("calculateTotalPrice() - Request received. Cart ID: {}", id);

        double totalPrice = cartService.calculateTotalPrice(id);
        log.info("calculateTotalPrice() - Total price calculated for Cart ID: {}. Total: {}", id, totalPrice);

        return ResponseEntity.ok(totalPrice);
    }

    /**
     * Creates a new cart.
     *
     * @param cartDTO the CartDTO containing the cart information to create.
     * @return the created CartDTO.
     */
    @PostMapping
    public CartDTO createCart(@RequestBody CartDTO cartDTO) {
        log.info("createCart() - Request received. Creating cart for User ID: {}", cartDTO.getUserId());

        CartDTO createdCart = cartService.creatCart(cartDTO);
        log.info("createCart() - Cart created successfully. Cart ID: {}", createdCart.getCartId());

        return createdCart;
    }

    /**
     * Adds a book to a specific cart.
     *
     * @param cartId   the ID of the cart to which the book is to be added.
     * @param bookId   the ID of the book to add to the cart.
     * @param quantity the quantity of the book to add.
     * @return the updated CartDTO after the book has been added.
     */
    @PostMapping("/{cartId}/add/{bookId}")
    public CartDTO addBookToCart(@PathVariable Long cartId, @PathVariable Long bookId, @RequestParam int quantity) {
        log.info("addBookToCart() - Request received. Cart ID: {}, Book ID: {}, Quantity: {}", cartId, bookId, quantity);

        CartDTO updatedCart = cartService.addABookToCart(cartId, bookId, quantity);
        log.info("addBookToCart() - Book added to Cart ID: {}. Updated Cart Books: {}", cartId, updatedCart.getBooks().size());

        return updatedCart;
    }

    /**
     * Removes a book from a specific cart.
     *
     * @param cartId the ID of the cart from which the book is to be removed.
     * @param bookId the ID of the book to remove from the cart.
     */
    @DeleteMapping("/{cartId}/add/{bookId}")
    public void removeBookFromCart(@PathVariable Long cartId, @PathVariable Long bookId) {
        log.info("removeBookFromCart() - Request received. Cart ID: {}, Book ID: {}", cartId, bookId);

        cartService.removeBook(cartId, bookId);
        log.info("removeBookFromCart() - Book removed from Cart ID: {}", cartId);
    }

    /**
     * Deletes a specific cart by its ID.
     *
     * @param id the ID of the cart to delete.
     */
    @DeleteMapping("/{cartId}")
    public void deleteCart(@PathVariable Long id) {
        log.info("deleteCart() - Request received. Cart ID: {}", id);

        cartService.deleteCart(id);
        log.info("deleteCart() - Cart deleted successfully. Cart ID: {}", id);
    }

}
