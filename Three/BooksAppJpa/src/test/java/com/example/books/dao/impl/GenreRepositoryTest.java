package com.example.books.dao.impl;

import com.example.books.dao.GenreRepository;
import com.example.books.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import javax.persistence.PersistenceException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("unused")
@DataJpaTest
@Import({GenreRepositoryJpa.class})
//@Sql(scripts = {"/test-data.sql"}, config = @SqlConfig(encoding = "utf-8"))
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void shouldReturnAllTheGenresFromTheDatabase() {
        final int initialGenreAmount = 4;
        List<Genre> result = genreRepository.findAll();
        assertThat(result).hasSize(initialGenreAmount);
    }

    @Test
    public void shouldReturnSingleGenreById() {
        final long genreId = 1L;
        Optional<Genre> result = genreRepository.findById(genreId);
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(genreId);
    }

    @Test
    public void shouldReturnAllTheGenresByBookId() {
        final long bookId = 1L;
        final int expectedGenresAmount = 2;
        Set<Genre> result = genreRepository.findByBookId(bookId);
        assertThat(result).hasSize(expectedGenresAmount);
    }

    @Test
    public void shouldCreateNewGenre() {
        Genre genre = new Genre("Love story");
        Optional<Genre> result = genreRepository.create(genre);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isNotEqualTo(0);

        Optional<Genre> newOne = genreRepository.findById(result.get().getId());
        assertThat(newOne).isPresent();
        assertThat(genre.getName()).isEqualTo(newOne.get().getName());
    }

    @Test
    public void shouldUpdateExistingGenre() {
        final String newName = "Science Fiction";
        final long genreId = 1L;
        Optional<Genre> genre = genreRepository.findById(genreId);
        assertThat(genre).isPresent();
        assertThat(genre.get().getName()).isNotEqualTo(newName);

        genre.get().setName(newName);
        Genre result = genreRepository.update(genre.get());
        Optional<Genre> newOne = genreRepository.findById(result.getId());
        assertThat(newOne).isPresent();
        assertThat(genre.get()).isEqualTo(newOne.get());
    }

    @Test
    void shouldFailWhenTryingToDeleteGenreBecauseOfIntegrityViolation() {
        final long bookId = 1L;
        final int initialGenresAmount = 2;
        Set<Genre> genresBefore = genreRepository.findByBookId(bookId);
        assertThat(genresBefore).hasSize(initialGenresAmount);

        long toDelete = genresBefore.iterator().next().getId();

        assertThatThrownBy(() -> {
            genreRepository.deleteById(toDelete);
            entityManager.flush();
        }, "PersistenceException was expected").isInstanceOf(PersistenceException.class);

        entityManager.clear();
        Set<Genre> genresAfter = genreRepository.findByBookId(1L);
        assertThat(genresBefore).hasSize(initialGenresAmount);

        Optional<Genre> genreAfter = genreRepository.findById(toDelete);
        assertThat(genreAfter).isPresent();
    }

    @Test
    void shouldSuccessfullyDeleteGenre() {
        List<Genre> result = genreRepository.findAll();

        Optional<Genre> genre = genreRepository.findAll().stream().filter(g -> new String(g.getName().getBytes(), StandardCharsets.UTF_8).equals("Детская литература")).findFirst();
        assertThat(genre).isPresent();
        long id = genre.get().getId();
        genreRepository.deleteById(id);

        Optional<Genre> after = genreRepository.findById(id);
        assertThat(after).isNotPresent();
    }
}