package com.example.books.dao.jdbc;

import com.example.books.dao.GenreRepository;
import com.example.books.model.Author;
import com.example.books.model.Book;
import com.example.books.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@JdbcTest(properties = "spring.shell.interactive.enabled=false")
@ComponentScan(basePackages = "com.example.books.dao.jdbc")
class BookRepositoryJdbcTest {

    @Autowired
    private BookRepositoryJdbc bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void getAll() {
        List<Book> bookList = bookRepository.findAll();
        assertEquals(3, bookList.size());
        System.out.println(bookList);
        List<Book> fandorin = bookList.stream().filter(b -> "12345678".equals(b.getIsbn())).collect(Collectors.toList());
        assertEquals(1, fandorin.size());
        assertEquals(2, fandorin.get(0).getGenres().size());
    }

    @Test
    void addBookTestOk() {
        Book book = new Book();
        book.setAuthor(new Author(1, "Борис Акунин"));
        book.setTitle("The book");
        book.setIsbn("1234");
        book.setIssueYear(1990);
        book.setGenres(new HashSet<>(Arrays.asList(new Genre(1L, "Fiction"), new Genre(2L, "Science"))));

        List<Book> before = bookRepository.findAll();
        assertEquals(3, before.size());
        assertNull(book.getId());
        Book added = bookRepository.add(book).get();
        assertNotNull(added.getId());
        List<Book> after = bookRepository.findAll();
        assertEquals(4, after.size());
        List<Genre> genres = genreRepository.findByBookId(added.getId());
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
            Book added = bookRepository.add(book).get();
        } catch (Exception e) {

        }
        List<Book> after = bookRepository.findAll();
        assertEquals(3, after.size());
    }

    @Test
    void delete() {
        List<Genre> genresBefore = genreRepository.findByBookId(1);
        assertFalse(genresBefore.isEmpty());
        assertTrue(bookRepository.findById(1).isPresent());
        bookRepository.delete(1);
        assertFalse(bookRepository.findById(1).isPresent());
        List<Genre> genresAfter = genreRepository.findByBookId(1);
        assertTrue(genresAfter.isEmpty());
    }

    @Test
    void getById() {
        List<Book> bookList = bookRepository.findAll();
        Book toFind = bookList.get(0);
        Book foundById = bookRepository.findById(toFind.getId()).get();
        assertEquals(toFind, foundById);
    }
}