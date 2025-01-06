package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Cart;
import com.example.bookstore.model.Order;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartRepository;
import com.example.bookstore.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, BookRepository bookRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.cartService = cartService;
    }

    // get all orders
    public List<Order> allOrders() {

        return orderRepository.findAll();
    }

    //get all orders for a particular date
    public List<Order> filterOrdersByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return orderRepository.findByOrderDateBetween(startOfDay, endOfDay);
    }

    //get an order by id
    public Optional<Order> getOrderById(Long orderId) {

        return orderRepository.findById(orderId);
    }

    //get total bill
    public Double totalBill(Long orderId) {
        // Fetch the order by its ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Return the total bill for the order
        return order.getTotal();
    }

    //cancel an order
    public void cancelOrder(Long orderId) {

        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            throw new RuntimeException("Book not found with id: " + orderId);
        }
    }

    public Optional<Order> convertCartToOrder(Long userId, Long cartId) {
        // Get the cart for the user
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isEmpty()) {
            throw new RuntimeException("Cart not found for user with id: " + userId);
        }

        Cart cart = cartOptional.get();

        // Calculate the total price (Assuming you can calculate it based on the books and their prices)
        double totalPrice = cartService.calculateTotalPrice(cart.getCartId());

        // Create the order
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setBooks(cart.getBooks()); // Copy books from cart to order
        order.setTotal(totalPrice);
        order.setOrderDate(LocalDateTime.now());
//        order.setOrderStatus("Pending");  // Or another status as required

        // Save the order
        orderRepository.save(order);

        // Clear the cart or mark as converted (depends on business logic)
        cartRepository.deleteById(cartId); // Or you could mark it as converted if you prefer

        return Optional.of(order);  // Return the created order

    }

}
