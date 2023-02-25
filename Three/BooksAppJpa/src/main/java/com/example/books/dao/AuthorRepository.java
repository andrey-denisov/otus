package com.example.books.dao;

import com.example.books.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    List<Author> findAll();

    Optional<Author> findById(long id);

    Optional<Author> create(Author author);

    Author update(Author author);

    void deleteById(long id);

}
