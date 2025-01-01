package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    //save a book
    public Book saveBook(Book book){
        return bookRepository.save(book);
    }

    //get all books
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    //get a book by id
    public Optional<Book> getBookById(Long id){
        return bookRepository.findById(id);
    }

    //delete a book
    public void deleteBook(Long id){
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new RuntimeException("Book not found with id: " + id);
        }
    }

}

