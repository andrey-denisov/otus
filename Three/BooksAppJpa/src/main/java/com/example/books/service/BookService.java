package com.example.books.service;

import com.example.books.model.Book;

import java.util.List;

public interface BookService {
    List<Book> findAll();
    Book findById(long id);
    Book add(Book book);
    Book add(String title, String isbn, int issued, long authorId, String[] genreIds );
}
