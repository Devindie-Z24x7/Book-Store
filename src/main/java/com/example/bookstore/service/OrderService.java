package com.example.bookstore.service;

import com.example.bookstore.service.dto.OrderDTO;
import com.example.bookstore.model.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<OrderDTO> allOrders();

    List<OrderDTO> filterOrdersByDate(LocalDate date);

    Optional<OrderDTO> getOrderById(Long orderId);

    Double totalBill(Long orderId);

    void cancelOrder(Long orderId);

    Optional<Order> convertCartToOrder(Long userId, Long cartId);

}
