package com.example.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name="orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ElementCollection
    @CollectionTable(name = "order_books", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyJoinColumn(name = "book_id")
    @Column(name = "quantity")
    private Map<Long,Integer> books;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to the User table
    private User user;

    private Double total;
    private LocalDateTime orderDate;
    private String orderStatus;

    // Constructor to create an Order from a Cart
    public Order(Cart cart, Double total, LocalDateTime orderDate, String orderStatus) {
        this.user = cart.getUser();
        this.books = cart.getBooks(); // Copy books and quantities from the cart
        this.total = total;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }
}


