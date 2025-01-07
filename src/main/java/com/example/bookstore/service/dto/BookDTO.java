package com.example.bookstore.service.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private double price;

}
