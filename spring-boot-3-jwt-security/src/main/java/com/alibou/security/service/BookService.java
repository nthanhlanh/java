package com.alibou.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.alibou.security.domain.Book;
import com.alibou.security.dto.BookRequest;
import com.alibou.security.repository.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;

    public void save(BookRequest request) {
        var book = Book.builder()
                .id(request.getId())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .build();
        repository.save(book);
    }

    public List<Book> findAll() {
        return repository.findAll();
    }
}
