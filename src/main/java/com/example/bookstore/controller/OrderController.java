package com.example.bookstore.controller;

import com.example.bookstore.service.OrderService;
import com.example.bookstore.service.dto.OrderDTO;
import com.example.bookstore.service.implementation.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller for Orders
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderServiceImpl orderService) {

        this.orderService = orderService;
    }

    /**
     * Retrieves all orders.
     *
     * @return a ResponseEntity containing a list of all OrderDTO objects.
     */
    @GetMapping("/all")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {

        log.info("getAllOrders() - Request received to retrieve all orders.");

        List<OrderDTO> orders = orderService.allOrders();

        log.info("getAllOrders() - Retrieved {} orders.", orders.size());
        return ResponseEntity.ok(orders);

    }

    /**
     * Retrieves all orders filtered by a specific date.
     *
     * @param date the date to filter orders by (in YYYY-MM-DD format).
     * @return a ResponseEntity containing a list of OrderDTO objects filtered by the specified date.
     */
    @GetMapping("/filter")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByDate(@RequestParam("date") String date) {

        log.info("getAllOrdersByDate() - Request received to filter orders by date: {}", date);

        LocalDate localDate = LocalDate.parse(date);
        List<OrderDTO> orderDTOs = orderService.filterOrdersByDate(localDate);

        log.info("getAllOrdersByDate() - Retrieved {} orders for date: {}", orderDTOs.size(), date);

        return ResponseEntity.ok(orderDTOs);

    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order to retrieve.
     * @return a ResponseEntity containing the OrderDTO if found, or a 404 status if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {

        log.info("getOrderById() - Request received. Order ID: {}", orderId);
        Optional<OrderDTO> orderDTOs = orderService.getOrderById(orderId);

        return orderDTOs.map(orderDTO -> {
            log.info("getOrderById() - Order found. Order ID: {}", orderId);
            return ResponseEntity.ok(orderDTO);
        }).orElseGet(() -> {
            log.warn("getOrderById() - Order not found. Order ID: {}", orderId);
            return ResponseEntity.notFound().build();
        });

    }
    /**
     * Retrieves the total bill for a specific order.
     *
     * @param id the ID of the order.
     * @return a ResponseEntity containing the total bill amount as a Double, or a 404 status if the order is not found.
     */
    @GetMapping("/{id}/total")
    public ResponseEntity<Double> getTotalBill(@PathVariable Long id) {
        log.info("getTotalBill() - Request received. Order ID: {}", id);
        Double total = orderService.totalBill(id); // Assuming totalBill returns a Double or throws an exception if not found

        if (total != null) {
            log.info("getTotalBill() - Total bill calculated for Order ID: {}. Total: {}", id, total);
            return ResponseEntity.ok(total);
        } else {
            log.warn("getTotalBill() - Order not found for ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cancels an order by its ID.
     *
     * @param id the ID of the order to cancel.
     */
    @DeleteMapping("/{orderId}")
    public void deleteCart(@PathVariable Long id) {

        log.info("deleteCart() - Request received to cancel Order ID: {}", id);

        orderService.cancelOrder(id);

        log.info("deleteCart() - Order ID: {} has been successfully canceled.", id);

    }

}
