package com.example.books.dao;

import com.example.books.model.Genre;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface GenreRepository {
    List<Genre> findAll();

    Genre findById(long id);

    Set<Genre> findByBookId(long bookId);

    @Transactional
    Genre create(Genre genre);

    @Transactional
    Genre update(Genre genre);

    @Transactional
    Genre deleteById(long id);
}
