package com.example.bookstore.service.implementation;

import com.example.bookstore.service.dto.OrderDTO;
import com.example.bookstore.service.mapper.OrderMapper;
import com.example.bookstore.model.Cart;
import com.example.bookstore.model.Order;
import com.example.bookstore.repository.CartRepository;
import com.example.bookstore.repository.OrderRepository;
import com.example.bookstore.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartServiceImpl cartService;

    public OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository, CartServiceImpl cartService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
    }

    /**
     * Get all the orders
     * @return List of Orders
     */
    @Override
    public List<OrderDTO> allOrders() {

        return orderRepository.findAll().stream()
                .map(OrderMapper::toOrderDTO)
                .collect(Collectors.toList());

    }

    /**
     * Get all orders for a particular date
     * @param date Date to filter the orders
     * @return List of orders for the particular date
     */
    @Override
    public List<OrderDTO> filterOrdersByDate(LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return orderRepository.findByOrderDateBetween(startOfDay,endOfDay).stream()
                .map(OrderMapper::toOrderDTO)
                .collect(Collectors.toList());

    }

    /**
     * Get an order by id
     * @param orderId id of the order
     * @return order of the particular id
     */
    @Override
    public Optional<OrderDTO> getOrderById(Long orderId) {

        return orderRepository.findById(orderId)
                .map(OrderMapper::toOrderDTO);
    }

    /**
     * Get total bill
     * @param orderId id of the order to check the total bill
     * @return total value of the bill
     */
    public Double totalBill(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return order.getTotal();
    }

    /**
     * Cancel an order
     * @param orderId Order id to cancel
     */
    @Override
    public void cancelOrder(Long orderId) {

        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            throw new RuntimeException("Book not found with id: " + orderId);
        }
    }

    /**
     * Convert a cart to an order
     * @param userId User id
     * @param cartId Cart id
     * @return Order of the user
     */
    @Override
    public Optional<Order> convertCartToOrder(Long userId, Long cartId) {

        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isEmpty()) {
            throw new RuntimeException("Cart not found for user with id: " + userId);
        }

        Cart cart = cartOptional.get();

        double totalPrice = cartService.calculateTotalPrice(cart.getCartId());

        Order order = createOrder(cart, totalPrice);

        orderRepository.save(order);

        cartRepository.deleteById(cartId);

        return Optional.of(order);

    }

    private Order createOrder(Cart cart, Double totalPrice) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setBooks(cart.getBooks()); // Copy books from cart to order
        order.setTotal(totalPrice);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

}
