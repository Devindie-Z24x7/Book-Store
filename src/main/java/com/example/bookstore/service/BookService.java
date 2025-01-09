package com.example.bookstore.service;

import com.example.bookstore.service.dto.BookDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookService {

    BookDTO saveBook(BookDTO bookDto);

    public List<BookDTO> getAllBooks();

    Optional<BookDTO> getBookById(Long id);

    void deleteBook(Long id);

    BookDTO updateBookByFields(Long id, Map<String, Object> fields);

}
