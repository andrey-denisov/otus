package com.example.books.dao.jdbc;

import com.example.books.dao.GenreRepository;
import com.example.books.model.Author;
import com.example.books.model.Book;
import com.example.books.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.*;

@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {BookRepositoryJdbc.class, GenreRepository.class}))
class BookRepositoryJdbcTest {

    @Autowired
    private BookRepositoryJdbc bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void shouldReturnAllBooksList() {
        final int foundAmount = 1;
        final int initialAmount = 3;
        final int genreAmount = 2;
        final String isbn = "12345678";
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList.size()).isEqualTo(initialAmount);
        List<Book> fandorin = bookList.stream().filter(b -> isbn.equals(b.getIsbn())).collect(Collectors.toList());
        assertThat(fandorin.size()).isEqualTo(foundAmount);
        assertThat(fandorin.get(0).getGenres().size()).isEqualTo(genreAmount);
    }

    @Test
    void shouldAddBookToDatabase() {
        Book book = new Book();
        book.setAuthor(new Author(1, "Борис Акунин"));
        book.setTitle("The book");
        book.setIsbn("1234");
        book.setIssueYear(1990);
        book.setGenres(new HashSet<>(Arrays.asList(new Genre(1L, "Fiction"), new Genre(2L, "Science"))));

        final int initialAmount = 3;
        final int finalAmount = 4;
        final int genresAmount = 2;
        List<Book> before = bookRepository.findAll();
        assertThat(before.size()).isEqualTo(initialAmount);
        assertThat(book.getId()).isNull();
        Optional<Book> added = bookRepository.add(book);
        assertThat(added).isPresent();
        assertThat(added.get().getId()).isNotNull();
        List<Book> after = bookRepository.findAll();
        assertThat(after.size()).isEqualTo(finalAmount);
        List<Genre> genres = genreRepository.findByBookId(added.get().getId());
        assertThat(genres.size()).isEqualTo(genresAmount);
    }

    @Test
    void shouldFailWhenTryingToAddBook() {
        try {
            Book book = new Book();
            book.setAuthor(new Author("Isaak Azimov"));
            book.setTitle("The book");
            book.setIsbn("1234");
            book.setIssueYear(1990);
            book.setGenres(new HashSet<>(Arrays.asList(null, new Genre(2L, "Science"))));

            assertThat(book.getId()).isNull();
            bookRepository.add(book);
        } catch (Exception e) {

        }
        final int finalAmount = 3;
        List<Book> after = bookRepository.findAll();
        assertThat(after.size()).isEqualTo(finalAmount);
    }

    @Test
    void shouldDeleteBook() {
        final long bookId = 1;
        List<Genre> genresBefore = genreRepository.findByBookId(bookId);
        assertThat(genresBefore.isEmpty()).isFalse();
        assertThat(bookRepository.findById(bookId).isPresent()).isTrue();
        bookRepository.delete(bookId);
        assertThat(bookRepository.findById(bookId).isPresent()).isFalse();
        List<Genre> genresAfter = genreRepository.findByBookId(bookId);
        assertThat(genresAfter.isEmpty()).isTrue();
    }

    @Test
    void shouldGetBookById() {
        List<Book> bookList = bookRepository.findAll();
        Book toFind = bookList.get(0);
        Optional<Book> foundById = bookRepository.findById(toFind.getId());
        assertThat(foundById).isPresent().get().isEqualTo(toFind);
    }
}