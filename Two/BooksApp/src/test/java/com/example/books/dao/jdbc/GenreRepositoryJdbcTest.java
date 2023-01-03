package com.example.books.dao.jdbc;

import com.example.books.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest(properties = "spring.shell.interactive.enabled=false")
@ComponentScan(basePackages = "com.example.books.dao.jdbc")
class GenreRepositoryJdbcTest {

    @Autowired
    private GenreRepositoryJdbc genreRepository;

    @Test
    void getAll() {
        List<Genre> result = genreRepository.findAll();
        assertEquals(4, result.size());
    }

    @Test
    void byBookId() {
        List<Genre> genres = genreRepository.findByBookId(1L);
        assertEquals(2, genres.size());
    }

    @Test
    void deleteById_Fail() {
        List<Genre> genresBefore = genreRepository.findByBookId(1L);
        assertEquals(2, genresBefore.size());
        long toDelete = genresBefore.get(0).getId();

        DataIntegrityViolationException thrown = assertThrows(DataIntegrityViolationException.class, () -> {
            genreRepository.deleteById(toDelete);
        }, "DataIntegrityViolationException was expected");

        List<Genre> genresAfter = genreRepository.findByBookId(1L);
        assertEquals(2, genresBefore.size());

        Genre genreAfter = genreRepository.findById(toDelete).get();
        assertNotNull(genreAfter);
    }

    @Test
    void deleteById_Ok() {
        Optional<Genre> genre = genreRepository.findAll().stream().filter(g -> g.getName().equals("Детская литература")).findFirst();
        assertTrue(genre.isPresent());
        long id = genre.get().getId();
        genreRepository.deleteById(id);

        assertFalse(genreRepository.findById(id).isPresent());
    }
}