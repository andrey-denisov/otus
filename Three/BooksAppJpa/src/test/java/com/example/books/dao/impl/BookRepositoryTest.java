package com.example.books.dao.impl;

import com.example.books.dao.BookRepository;
import com.example.books.model.Book;
import com.example.books.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("unused")
@DataJpaTest
@Import({BookRepositoryJpa.class, GenreRepositoryJpa.class, AuthorRepositoryJpa.class})
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepositoryJpa genreRepository;

    @Autowired
    private AuthorRepositoryJpa authorRepository;


    @Test
    void shouldReturnAllBooks() {
        final int initialBooksAmount = 3;
        List<Book> result = bookRepository.findAll();
        assertThat(result).hasSize(initialBooksAmount);
    }

    @Test
    public void shouldReturnBookById() {
        final long bookId = 1L;
        final int genresAmount = 2;
        Optional<Book> result = bookRepository.findById(bookId);
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(bookId);
        assertThat(result.get().getGenres()).hasSize(genresAmount);
    }

    @Test
    public void shouldDeleteBookById() {
        final long bookId = 1L;
        final int initialGenesAmount = 2;
        Set<Genre> genresOfBookToDelete = genreRepository.findByBookId(bookId);
        assertThat(genresOfBookToDelete).hasSize(initialGenesAmount);

        Optional<Book> toDelete = bookRepository.findById(bookId);
        assertThat(toDelete).isPresent();
        bookRepository.deleteById(bookId);
        entityManager.flush();
        entityManager.clear();
        Optional<Book> deleted = bookRepository.findById(bookId);
        assertThat(deleted).isNotPresent();

        Set<Genre> genresDeleted = genreRepository.findByBookId(bookId);
        assertThat(genresDeleted).isEmpty();
    }

    @Test
    public void shouldCreateBook() {

        final long bookId = 1L;
        final long authorId = 1L;

        Book book = new Book();
        book.setTitle("New Book");
        book.setIsbn("11111111");
        book.setIssueYear(1990);
        book.setAuthor(authorRepository.findById(authorId).orElse(null));

        Set<Genre> genres = new HashSet<>();
        genres.add(genreRepository.findById(1L).orElse(null));
        genres.add(genreRepository.findById(2L).orElse(null));
        book.setGenres(genres);

        Optional<Book> created = bookRepository.create(book);
        assertThat(created).isPresent();
        Optional<Book> found = bookRepository.findById(created.get().getId());
        assertThat(found).isPresent();
        assertThat(created.get()).isEqualTo(found.get());

        Set<Genre> genresOfCreated = genreRepository.findByBookId(found.get().getId());
        assertThat(genres).isEqualTo(genresOfCreated);
    }

    @Test
    public void shouldUpdateBook() {
        final long bookId = 1L;
        final long authorId = 2L;
        final long genreId = 3L;
        Book book = bookRepository.findById(bookId).orElse(null);
        assertThat(book).isNotNull();
        book.setTitle("New Book");
        book.setIsbn("11111111");
        book.setIssueYear(1990);
        book.setAuthor(authorRepository.findById(authorId).orElse(null));

        Set<Genre> genres = new HashSet<>();
        genres.add(genreRepository.findById(genreId).orElse(null));
        book.setGenres(genres);

        Book updated = bookRepository.update(book);

        Book found = bookRepository.findById(updated.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(updated).isEqualTo(found);
        assertThat(book).isEqualTo(found);

        Set<Genre> genresCreated = genreRepository.findByBookId(found.getId());
        assertThat(genres).isEqualTo(genresCreated);
    }
}
