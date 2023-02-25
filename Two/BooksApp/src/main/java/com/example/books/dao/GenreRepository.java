package com.example.books.dao;

import com.example.books.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    List<Genre> findAll();
    List<Genre> findByBookId(long bookId);
    Optional<Genre> findById(long id);
    void deleteById(long id);
}
