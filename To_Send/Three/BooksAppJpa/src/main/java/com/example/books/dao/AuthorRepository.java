package com.example.books.dao;

import com.example.books.model.Author;

import java.util.List;

public interface AuthorRepository {
    List<Author> findAll();

    Author findById(long id);

    Author create(Author author);

    Author update(Author author);

    Author deleteById(long id);

}
