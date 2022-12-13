package com.example.books.dao;

import com.example.books.model.Author;

import java.util.List;

public interface AuthorRepository {
    List<Author> findAll();
    Author create(String name);
    Author findById(long id);
    Author update(Author author);
    void deleteById(long id);
}
