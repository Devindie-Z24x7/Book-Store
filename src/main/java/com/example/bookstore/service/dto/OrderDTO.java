package com.example.bookstore.service.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderDTO {

    private Long orderId;
    private Long userId;
    private Map<Long, Integer> books;
    private Double total;
    private LocalDateTime orderDate;

}
