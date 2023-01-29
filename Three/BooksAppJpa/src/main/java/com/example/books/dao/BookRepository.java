package com.example.books.dao;

import com.example.books.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();

    Optional<Book> findById(long id);

    Optional<Book> create(Book book);

    void deleteById(long id);

    Book update(Book book);
}
