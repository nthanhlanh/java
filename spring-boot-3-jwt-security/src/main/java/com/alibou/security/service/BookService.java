package com.alibou.security.service;

import com.alibou.security.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alibou.security.domain.Book;
import com.alibou.security.repository.BookRepository;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Optional<Book> getBookById(UUID id) {
        return bookRepository.findById(id);
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> updateBook(UUID id, Book bookDetails) {
        return bookRepository.findById(id).map(book -> {
            book.setName(bookDetails.getName());
            book.setAuthor(bookDetails.getAuthor());
            return bookRepository.save(book);
        });
    }

    public void deleteBook(UUID id) {
        log.info("Attempting to soft delete book with ID: {}", id);
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

            book.setIsDeleted(true);

            bookRepository.save(book);
            log.info("Successfully soft deleted book with ID: {}", id);
        } else {
            log.error("Book not found with ID: {}", id);
            throw new EntityNotFoundException("Book not found with ID: " + id);
        }
    }
}
