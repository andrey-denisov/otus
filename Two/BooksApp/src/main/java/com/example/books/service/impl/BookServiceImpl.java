package com.example.books.service.impl;

import com.example.books.dao.BookRepository;
import com.example.books.model.Book;
import com.example.books.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null);
    }

    @Transactional
    @Override
    public Book add(Book book) {
        Optional<Book> result = bookRepository.add(book);
        return result.orElse(null);
    }
}
