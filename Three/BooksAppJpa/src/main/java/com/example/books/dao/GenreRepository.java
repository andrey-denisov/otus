package com.example.books.dao;

import com.example.books.model.Genre;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface GenreRepository {
    List<Genre> findAll();

    Optional<Genre> findById(long id);

    Set<Genre> findByBookId(long bookId);

    Optional<Genre> create(Genre genre);

    Genre update(Genre genre);

    void deleteById(long id);
}
