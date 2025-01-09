package com.example.bookstore.service.implementation;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.dto.BookDTO;
import com.example.bookstore.service.mapper.BookMapper;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
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

    public BookDTO updateBookByFields(Long id, Map<String, Object> fields) {
        Optional<Book> existingBook = bookRepository.findById(id);

        if (existingBook.isEmpty() ) {
            throw new RuntimeException("Book not found");
        }

        Book book = existingBook.get();

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Book.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, book, value);
            }
        });

        Book savedBook = bookRepository.save(book);
        return BookMapper.toBookDTO(savedBook);
    }

}

