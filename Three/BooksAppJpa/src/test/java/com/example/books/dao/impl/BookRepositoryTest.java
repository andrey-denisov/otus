package com.example.books.dao.impl;

import com.example.books.model.Book;
import com.example.books.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepositoryImpl bookRepository;

    @Autowired
    private AuthorRepositoryImpl authorRepository;

    @Autowired
    private GenreRepositoryImpl genreRepository;

    @Test
    void findAll() {
        List<Book> result = bookRepository.findAll();
        assertEquals(3, result.size());
    }

    @Test
    public void findById() {
        Book result = bookRepository.findById(1L);
        assertEquals(1L, result.getId());
        assertNotNull(result.getGenres());
        assertEquals(2, result.getGenres().size());
    }

    @Test
    public void delete() {
        Set<Genre> genresOfbookToDelete = genreRepository.findByBookId(1L);
        assertEquals(2, genresOfbookToDelete.size());

        Book toDelete = bookRepository.findById(1L);
        assertNotNull(toDelete);
        bookRepository.deleteById(1L);
        entityManager.flush();
        entityManager.clear();
        Book deleted = bookRepository.findById(1L);
        assertNull(deleted);

        Set<Genre> genresDeleted = genreRepository.findByBookId(1L);
        assertEquals(0, genresDeleted.size());
    }

    @Test
    public void create() {
        Book book = new Book();
        book.setTitle("New Book");
        book.setIsbn("11111111");
        book.setIssueYear(1990);
        book.setAuthor(authorRepository.findById(1L));

        Set<Genre> genres = new HashSet<>();
        genres.add(genreRepository.findById(1L));
        genres.add(genreRepository.findById(2L));
        book.setGenres(genres);

        Book created = bookRepository.create(book);

        Book found = bookRepository.findById(created.getId());
        assertEquals(created, found);

        Set<Genre> genresOfCreated = genreRepository.findByBookId(found.getId());
        assertEquals(genres, genresOfCreated);
    }

    @Test
    public void update() {
        Book book = bookRepository.findById(1L);
        book.setTitle("New Book");
        book.setIsbn("11111111");
        book.setIssueYear(1990);
        book.setAuthor(authorRepository.findById(2L));

        Set<Genre> genres = new HashSet<>();
        genres.add(genreRepository.findById(3L));
        book.setGenres(genres);

        Book updated = bookRepository.update(book);

        Book found = bookRepository.findById(updated.getId());
        assertEquals(updated, found);
        assertEquals(book, found);

        Set<Genre> genresOfCreated = genreRepository.findByBookId(found.getId());
        assertEquals(genres, genresOfCreated);
    }
}