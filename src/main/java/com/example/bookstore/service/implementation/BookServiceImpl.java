package com.example.bookstore.service.implementation;

import com.example.bookstore.service.dto.BookDTO;
import com.example.bookstore.service.mapper.BookMapper;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Saves a book to the database
     * @param bookDto The book entity to be saved
     * @return The saved book entity
     */
    @Override
    public BookDTO saveBook(BookDTO bookDto){
        return BookMapper.toBookDTO(bookRepository.save(BookMapper.toBookEntity(bookDto)));
    }

    /**
     * Retrieves all books from the database
     * @return A list of all books
     */
    @Override
    public List<BookDTO> getAllBooks(){

        return bookRepository.findAll()
                .stream()
                .map(BookMapper::toBookDTO )
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a book by id
     * @param id Book id
     * @return An Optional containing the book if found, or empty if not found
     */
    @Override
    public Optional<BookDTO> getBookById(Long id){
        return bookRepository.findById(id)
                .map(BookMapper::toBookDTO);
    }

    /**
     * Deletes a book by its ID
     * If the book does not exist, a RuntimeException is thrown
     * @param id Book id to delete
     */
    @Override
    public void deleteBook(Long id){
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new RuntimeException("Book not found with id: " + id);
        }
    }

}

