package com.example.books.dao.jdbc;

import com.example.books.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {GenreRepositoryJdbc.class}))
class GenreRepositoryJdbcTest {

    @Autowired
    private GenreRepositoryJdbc genreRepository;

    @Test
    void shouldReturnAllGenresList() {
        final int initialGenresAmount = 4;
        List<Genre> result = genreRepository.findAll();
        assertThat(result.size()).isEqualTo(initialGenresAmount);
    }

    @Test
    void shouldReturnBookById() {
        final long bookId = 1L;
        final int genresAmount = 2;
        List<Genre> genres = genreRepository.findByBookId(bookId);
        assertThat(genres.size()).isEqualTo(genresAmount);
    }

    @Test
    void shouldFailWhileDeletingGenreById() {
        final long bookId = 1L;
        final int initialAmount = 2;
        List<Genre> genresBefore = genreRepository.findByBookId(bookId);
        assertThat(genresBefore.size()).isEqualTo(initialAmount);
        long toDelete = genresBefore.get(0).getId();

        assertThatThrownBy(() -> genreRepository.deleteById(toDelete)).isInstanceOf(DataIntegrityViolationException.class);

        List<Genre> genresAfter = genreRepository.findByBookId(bookId);
        assertThat(genresAfter.size()).isEqualTo(initialAmount);

        Optional<Genre> genreAfter = genreRepository.findById(toDelete);
        assertThat(genreAfter).isPresent();
    }

    @Test
    void shouldDeleteGenreById() {
        final String genreName = "Детская литература";
        Optional<Genre> genre = genreRepository.findAll().stream().filter(g -> g.getName().equals(genreName)).findFirst();
        assertThat(genre).isPresent();
        long id = genre.get().getId();
        genreRepository.deleteById(id);
        assertThat(genreRepository.findById(id)).isNotPresent();
    }
}