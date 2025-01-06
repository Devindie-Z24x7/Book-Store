package com.example.bookstore.controller;

import com.example.bookstore.model.Order;
import com.example.bookstore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //get all orders
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> orders = orderService.allOrders();
        return ResponseEntity.ok(orders);
    }

    //get all orders
    @GetMapping("/filter")
    public ResponseEntity<List<Order>> getAllOrdersByDate(@RequestParam("date") String date ){
        LocalDate localDate = LocalDate.parse(date);
        List<Order> orders = orderService.filterOrdersByDate(localDate);
        return ResponseEntity.ok(orders);
    }

    //get order by id
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId){
        Optional<Order> order = orderService.getOrderById(orderId);
        return order.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    //get the total amount of the order
    @GetMapping("/{id}/total")
    public ResponseEntity<Double> getTotalBill(@PathVariable Long id){
        try{
            Double total = orderService.totalBill(id);
            return ResponseEntity.ok(total);
        }
        catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    //canceling an order
    @DeleteMapping("/{orderId}")
    public void deleteCart(@PathVariable Long id) {
        orderService.cancelOrder(id);
    }


}
