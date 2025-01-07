package com.example.bookstore.service.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserCreateDTO {

    private Long id;
    private String username;
    private String password;

}
