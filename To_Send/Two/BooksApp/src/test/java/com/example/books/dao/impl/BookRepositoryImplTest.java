package com.example.books.dao.impl;

import com.example.books.dao.AuthorRepository;
import com.example.books.dao.GenreRepository;
import com.example.books.model.Author;
import com.example.books.model.Book;
import com.example.books.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
class BookRepositoryImplTest {

    @Autowired
    private BookRepositoryImpl bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void getAll() {
        List<Book> bookList = bookRepository.getAll();
        assertEquals(3, bookList.size());
        System.out.println(bookList);
        List<Book> fandorin = bookList.stream().filter(b -> "12345678".equals(b.getIsbn())).collect(Collectors.toList());
        assertEquals(1, fandorin.size());
        assertEquals(2, fandorin.get(0).getGenres().size());
    }

    @Test
    void addBookTestOk() throws SQLException {
        Book book = new Book();
        book.setAuthor(authorRepository.findAll().get(0));
        book.setTitle("The book");
        book.setIsbn("1234");
        book.setIssueYear(1990);
        book.setGenres(new HashSet<>(Arrays.asList(new Genre(1L, "Fiction"), new Genre(2L, "Science"))));

        List<Book> before = bookRepository.getAll();
        assertEquals(3, before.size());
        assertNull(book.getId());
        Book added = bookRepository.add(book);
        assertNotNull(added.getId());
        List<Book> after = bookRepository.getAll();
        assertEquals(4, after.size());
        List<Genre> genres = genreRepository.byBookId(added.getId());
        assertEquals(2, genres.size());
    }

    @Test
    void addBookTestFailed() {
        try {
            Book book = new Book();
            book.setAuthor(new Author("Isaak Azimov"));
            book.setTitle("The book");
            book.setIsbn("1234");
            book.setIssueYear(1990);
            book.setGenres(new HashSet<>(Arrays.asList(null, new Genre(2L, "Science"))));

            assertNull(book.getId());
            Book added = bookRepository.add(book);
        } catch (Exception e) {

        }
        List<Book> after = bookRepository.getAll();
        assertEquals(3, after.size());
    }

    @Test
    void delete() {
        List<Genre> genresBefore = genreRepository.byBookId(1);
        assertFalse(genresBefore.isEmpty());
        assertNotNull(bookRepository.byId(1));
        bookRepository.delete(1);
        assertNull(bookRepository.byId(1));
        List<Genre> genresAfter = genreRepository.byBookId(1);
        assertTrue(genresAfter.isEmpty());
    }

    @Test
    void getById() {
        List<Book> bookList = bookRepository.getAll();
        Book toFind = bookList.get(0);
        Book foundById = bookRepository.byId(toFind.getId());
        assertEquals(toFind, foundById);
    }
}