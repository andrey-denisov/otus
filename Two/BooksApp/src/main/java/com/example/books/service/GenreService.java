package com.example.books.service;

import com.example.books.model.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();
    List<Genre> findByBookId(long bookId);
    Genre findById(long id);
    void deleteById(long id);
}
