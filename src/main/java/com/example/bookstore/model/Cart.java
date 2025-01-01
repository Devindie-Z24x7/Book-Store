package com.example.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
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
    private Long carId;


    @ElementCollection
    @CollectionTable(name = "cart_books", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "book_id") // The key (book) is a reference to the books table
    @Column(name = "quantity")         // The value column for quantity
    private Map<Long,Integer> books;
    private int UserId;

}
