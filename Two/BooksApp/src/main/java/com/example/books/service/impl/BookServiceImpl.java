package com.example.books.service.impl;

import com.example.books.dao.BookRepository;
import com.example.books.model.Book;
import com.example.books.model.Genre;
import com.example.books.service.AuthorService;
import com.example.books.service.BookService;
import com.example.books.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final GenreService genreService;

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

    @Override
    public Book add(String title, String isbn, int issued, long authorId, String[] genreIds) {
        Book book = new Book();
        book.setTitle(title);
        book.setIssueYear(issued);
        book.setIsbn(isbn);
        book.setAuthor(authorService.findById(1L));

        // TODO: по-хорошему, здесь наверное, лучше было бы сразу получать список жанров по списку id, и
        // сразу добавлять списком в поле genres
        Set<Genre> genres = new HashSet<>();
        for (String s : genreIds) {
            Genre genre = genreService.findById(Long.parseLong(s));
            if(Objects.nonNull(genre)) {
                genres.add(genre);
            }
        }
        book.setGenres(genres);
        return this.add(book);
    }


}
