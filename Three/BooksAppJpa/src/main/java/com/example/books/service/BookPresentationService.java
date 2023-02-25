package com.example.books.service;

public interface BookPresentationService {
    String listAllBooks();
    String bookByID(long id);
    String addBook(String title, String isbn, int issued, long authorId, String genreId);
}
