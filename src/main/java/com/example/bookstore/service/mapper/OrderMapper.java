package com.example.bookstore.service.mapper;

import com.example.bookstore.service.dto.OrderDTO;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.User;

public class OrderMapper {

    public static OrderDTO toOrderDTO(Order order) {
        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getId())
                .books(order.getBooks())
                .total(order.getTotal())
                .orderDate(order.getOrderDate())
                .build();
    }

    public static Order toOrderEntity(OrderDTO orderDTO, User user) {
        Order order = new Order();
        order.setOrderId(orderDTO.getOrderId());
        order.setUser(user);
        order.setBooks(orderDTO.getBooks());
        order.setTotal(orderDTO.getTotal());
        order.setOrderDate(orderDTO.getOrderDate());
        return order;
    }

}
