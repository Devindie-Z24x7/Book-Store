package com.example.bookstore.service.dto;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CartDTO {

    private Long cartId;
    private Long userId;
    private Map<Long, Integer> books;

}
