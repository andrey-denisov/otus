package com.example.books.service;

import com.example.books.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreService {
    List<Genre> findAll();
    Set<Genre> findByBookId(long bookId);
    Genre findById(long id);
    void deleteById(long id);
}
