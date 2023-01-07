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
        List<Genre> result = genreRepository.findAll();
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    void shouldReturnBookById() {
        List<Genre> genres = genreRepository.findByBookId(1L);
        assertThat(genres.size()).isEqualTo(2);
    }

    @Test
    void shouldFailWhileDeletingGenreById() {
        List<Genre> genresBefore = genreRepository.findByBookId(1L);
        assertThat(genresBefore.size()).isEqualTo(2);
        long toDelete = genresBefore.get(0).getId();

        assertThatThrownBy(() -> genreRepository.deleteById(toDelete)).isInstanceOf(DataIntegrityViolationException.class);

        List<Genre> genresAfter = genreRepository.findByBookId(1L);
        assertThat(genresAfter.size()).isEqualTo(2);

        Optional<Genre> genreAfter = genreRepository.findById(toDelete);
        assertThat(genreAfter).isPresent();
    }

    @Test
    void shouldDeleteGenreById() {
        Optional<Genre> genre = genreRepository.findAll().stream().filter(g -> g.getName().equals("Детская литература")).findFirst();
        assertThat(genre).isPresent();
        long id = genre.get().getId();
        genreRepository.deleteById(id);
        assertThat(genreRepository.findById(id)).isNotPresent();
    }
}