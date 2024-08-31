package com.alibou.security.service;

import com.alibou.security.exception.EntityNotFoundException;
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
        // Tìm đối tượng theo ID
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()) {
            // Lấy đối tượng Book từ Optional
            Book book = optionalBook.get();

            // Đặt trạng thái xóa mềm
            book.setIsDeleted(true);

            // Lưu đối tượng đã được cập nhật
            bookRepository.save(book);
        } else {
            // Xử lý trường hợp không tìm thấy đối tượng
            throw new EntityNotFoundException("Book not found with ID: " + id);
        }
    }
}
