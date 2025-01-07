package com.example.bookstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name="orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Double total;
    private LocalDateTime orderDate;

    public Order(Cart cart, Double total, LocalDateTime orderDate, String orderStatus) {
        this.user = cart.getUser();
        this.books = cart.getBooks();
        this.total = total;
        this.orderDate = orderDate;
    }
}


