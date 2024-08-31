package com.alibou.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alibou.security.domain.Book;
import com.alibou.security.repository.BookRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

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
        bookRepository.deleteById(id);
    }
}
