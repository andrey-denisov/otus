package com.example.books.dao.impl;

import com.example.books.dao.GenreRepository;
import com.example.books.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
class GenreRepositoryImplTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void getAll() {
        List<Genre> result = genreRepository.getAll();
        assertEquals(4, result.size());
    }

    @Test
    void byBookId() {
        List<Genre> genres = genreRepository.byBookId(1L);
        assertEquals(2, genres.size());
    }

    @Test
    void deleteById_Fail() {
        List<Genre> genresBefore = genreRepository.byBookId(1L);
        assertEquals(2, genresBefore.size());
        long toDelete = genresBefore.get(0).getId();

        DataIntegrityViolationException thrown = assertThrows(DataIntegrityViolationException.class, () -> {
            genreRepository.deleteById(toDelete);
        }, "DataIntegrityViolationException was expected");

        List<Genre> genresAfter = genreRepository.byBookId(1L);
        assertEquals(2, genresBefore.size());

        Genre genreAfter = genreRepository.byId(toDelete);
        assertNotNull(genreAfter);
    }

    @Test
    void deleteById_Ok() {
        Genre genre = genreRepository.getAll().stream().filter(g -> g.getName().equals("Детская литература")).findFirst().get();
        assertNotNull(genre);
        long id = genre.getId();
        genreRepository.deleteById(id);

        Genre after = genreRepository.byId(id);
        assertNull(after);
    }
}