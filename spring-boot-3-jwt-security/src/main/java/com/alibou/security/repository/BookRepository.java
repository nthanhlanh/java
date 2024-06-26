package com.alibou.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alibou.security.domain.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
