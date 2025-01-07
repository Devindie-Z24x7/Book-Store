package com.example.bookstore.service.mapper;

import com.example.bookstore.service.dto.BookDTO;
import com.example.bookstore.model.Book;

public class BookMapper {

    public static BookDTO toBookDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .build();
    }

    public static Book toBookEntity(BookDTO bookDTO) {
        return new Book(bookDTO.getId(), bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPrice());
    }


}
