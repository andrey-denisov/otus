package com.example.books.dao;

import com.example.books.model.Book;

import javax.transaction.Transactional;
import java.util.List;

public interface BookRepository {
    List<Book> findAll();

    Book findById(long id);

    @Transactional
    Book create(Book book);

    @Transactional
    Book deleteById(long id);

    @Transactional
    Book update(Book book);
}
