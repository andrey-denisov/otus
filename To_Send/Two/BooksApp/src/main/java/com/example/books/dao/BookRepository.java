package com.example.books.dao;

import com.example.books.model.Book;

import java.sql.SQLException;
import java.util.List;

public interface BookRepository {
    Book add(Book book) throws SQLException;

    void update(Book Book);

    void delete(long id);

    List<Book> getAll();

    Book byId(long id);

}
