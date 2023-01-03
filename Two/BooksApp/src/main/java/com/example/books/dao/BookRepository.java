package com.example.books.dao;

import com.example.books.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> add(Book book);

    void update(Book Book);

    void delete(long id);

    List<Book> findAll();

    Optional<Book> findById(long id);
}
