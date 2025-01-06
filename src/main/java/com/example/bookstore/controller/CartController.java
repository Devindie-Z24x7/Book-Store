package com.example.bookstore.controller;
import com.example.bookstore.model.Cart;
import com.example.bookstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    @Autowired
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/all")
    public List<Cart> displayAllCarts(){
        return cartService.getAllCarts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> displayCart(@PathVariable Long id){
        Optional<Cart> cart = cartService.displayCart(id);
        return cart.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // View books in a cart
    @GetMapping("/{id}/books")
    public ResponseEntity<?> viewBooksInCart(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.viewBooksInCart(id));
    }

    // Calculate total price of the cart
    @GetMapping("/{id}/totalPrice")
    public ResponseEntity<Double> calculateTotalPrice(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.calculateTotalPrice(id));
    }

    @PostMapping
    public Cart createCart(@RequestBody Cart cart){
        return cartService.creatCart(cart);
    }

    @PostMapping("/{cartId}/add/{bookId}")   //POST /api/v1/carts/1/add-book/5?quantity=3
    public Cart addBookToCart(@PathVariable Long cartId, @PathVariable Long bookId, @RequestParam int quantity){
        return cartService.addABookToCart(cartId, bookId, quantity);
    }

    @DeleteMapping("/{cartId}/add/{bookId}")   //POST /api/v1/carts/1/add-book/5?quantity=3
    public void removeBookFromCart(@PathVariable Long cartId, @PathVariable Long bookId){
        cartService.removeBook(cartId, bookId);
    }

    @DeleteMapping("/{cartId}")
    public void deleteCart(@PathVariable Long id) {
        cartService.deleteBook(id);
    }

}
