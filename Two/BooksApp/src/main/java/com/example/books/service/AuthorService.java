package com.example.books.service;

import com.example.books.model.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
    Author findById(long id);
}
