package com.example.books.dao.impl;

import com.example.books.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan
class GenreRepositoryTest {

    @Autowired
    private GenreRepositoryImpl genreRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findAll() {
        List<Genre> result = genreRepository.findAll();
        assertEquals(4, result.size());
    }

    @Test
    public void findById() {
        Genre result = genreRepository.findById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void findByBookId() {
        Set<Genre> result = genreRepository.findByBookId(1L);
        assertEquals(2L, result.size());
    }

    @Test
    public void create() {
        Genre genre = new Genre("Love story");
        Genre result = genreRepository.create(genre);

        assertNotEquals(0, result.getId());

        Genre newOne = genreRepository.findById(result.getId());
        assertEquals(genre.getName(), newOne.getName());
    }

    @Test
    public void update() {
        final String newName = "Science Fiction";
        Genre genre = genreRepository.findById(1L);
        assertNotEquals(newName, genre.getName());

        genre.setName(newName);
        Genre result = genreRepository.update(genre);

        Genre newOne = genreRepository.findById(result.getId());
        assertEquals(genre.getId(), newOne.getId());
        assertEquals(genre.getName(), newOne.getName());
    }

    @Test
    void deleteById_Fail() {
        Set<Genre> genresBefore = genreRepository.findByBookId(1L);
        assertEquals(2, genresBefore.size());
        long toDelete = genresBefore.iterator().next().getId();

        assertThrows(PersistenceException.class, () -> {
            genreRepository.deleteById(toDelete);
            entityManager.flush();
        }, "PersistenceException was expected");

        entityManager.clear();
        Set<Genre> genresAfter = genreRepository.findByBookId(1L);
        assertEquals(2, genresBefore.size());

        Genre genreAfter = genreRepository.findById(toDelete);
        assertNotNull(genreAfter);
    }

    @Test
    void deleteById_Ok() {
        Genre genre = genreRepository.findAll().stream().filter(g -> g.getName().equals("Детская литература")).findFirst().get();
        assertNotNull(genre);
        long id = genre.getId();
        genreRepository.deleteById(id);

        Genre after = genreRepository.findById(id);
        assertNull(after);
    }


}