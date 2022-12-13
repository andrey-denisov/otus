package com.example.books.dao;

import com.example.books.model.Genre;

import java.util.List;

public interface GenreRepository {
    List<Genre> getAll();
    List<Genre> byBookId(long bookId);
    Genre byId(long id);
    void deleteById(long id);
}
