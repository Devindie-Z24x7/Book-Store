package com.example.bookstore.controller;

import com.example.bookstore.service.BookService;
import com.example.bookstore.service.dto.BookDTO;
import com.example.bookstore.service.implementation.BookServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Controller for books
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    private final BookService bookService;

    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    /**
     * Retrieves a list of all books.
     *
     * @return a list of all BookDTO objects.
     */
    @GetMapping("/all")
    public List<BookDTO> getAllBooks() {

        log.info("getAllBooks() - Request received.");

        List<BookDTO> books = bookService.getAllBooks();

        log.info("getAllBooks() - Response prepared with {} books.", books.size());
        return books;
    }


    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book to retrieve.
     * @return a ResponseEntity containing the BookDTO if found, or a 404 status if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {

        log.info("getBookById() - Request received. Book ID: {}", id);

        return bookService.getBookById(id)
                .map(bookDTO -> {
                    log.info("getBookById() - Book found. Book ID: {}", id);
                    return ResponseEntity.ok(bookDTO);
                })
                .orElseGet(() -> {
                    log.warn("getBookById() - Book not found. Book ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }


    /**
     * Creates a new book.
     *
     * @param bookDto the BookDTO containing the book information to create.
     * @return a ResponseEntity containing the created BookDTO.
     */
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDto) {

        log.info("createBook() - Request received. Book Title: {}", bookDto.getTitle());

        BookDTO savedBook = bookService.saveBook(bookDto);

        log.info("createBook() - Book created successfully. Book ID: {}", savedBook.getId());

        return ResponseEntity.ok(savedBook);
    }


    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to delete.
     * @return a ResponseEntity with a 204 status if the book is successfully deleted.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {

        log.info("deleteBook() - Request received. Book ID: {}", id);

        bookService.deleteBook(id);

        log.info("deleteBook() - Book deleted successfully. Book ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO> updateBookByFields(@PathVariable Long id, @RequestBody Map<String, Object> fields) {

        log.info("updateBookByFields() - Request received. Book ID: {}", id);

        BookDTO updatedBook = bookService.updateBookByFields(id, fields);

        log.info("updateBookByFields() - Book updated successfully. Book ID: {}", id);

        return ResponseEntity.ok(updatedBook);
    }

}
