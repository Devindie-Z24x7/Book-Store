package com.example.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Entity
@Table(name="carts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ElementCollection
    @CollectionTable(name = "cart_books", joinColumns = @JoinColumn(name = "cart_id")) // The cart_id is a reference to the carts table
    @MapKeyJoinColumn(name = "book_id") // The book_id is a reference to the books table
    @Column(name = "quantity")         // The value column for quantity
    private Map<Long,Integer> books;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
