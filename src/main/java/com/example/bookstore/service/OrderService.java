package com.example.bookstore.service;

import com.example.bookstore.model.Order;
import com.example.bookstore.repository.CartRepository;
import com.example.bookstore.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    public final CartService cartService;

    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    // get all orders
    public List<Order> allOrders(){
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
    public Double totalBill(Long orderId){
        // Fetch the order by its ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Return the total bill for the order
        return order.getTotal();
    }

    //cancel an order
    public void cancelOrder(Long orderId){
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            throw new RuntimeException("Book not found with id: " + orderId);
        }
    }

}
