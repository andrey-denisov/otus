package com.example.books.dao.impl;

import com.example.books.dao.AuthorRepository;
import com.example.books.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findAll() {
        List<Author> result = authorRepository.findAll();
        assertEquals(3, result.size());
    }

    @Test
    public void findById() {
        Author  result = authorRepository.findById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void create() {
        Author author = new Author("Panaev");
        Author result = authorRepository.create(author);

        assertNotEquals(0, result.getId());

        Author newOne = authorRepository.findById(result.getId());
        assertEquals(author.getName(), newOne.getName());
    }


    @Test
    public void update() {
        final String newName = "Skubichevskiy";
        Author author = authorRepository.findById(1L);
        assertNotEquals(newName, author.getName());

        author.setName(newName);
        Author result = authorRepository.update(author);

        Author newOne = authorRepository.findById(result.getId());
        assertEquals(author.getId(), newOne.getId());
        assertEquals(author.getName(), newOne.getName());
    }

    @Test
    void deleteBydIFail() {
        List<Author> before = authorRepository.findAll();
        assertEquals(3, before.size());
        assertThrows(PersistenceException.class, () -> {
            authorRepository.deleteById(1L);
            entityManager.flush();
        }, "PersistenceException was expected");
    }

    @Test
    void deleteBydOk() {
        Author pushkin = authorRepository.create(new Author("Pushkin"));
        List<Author> before = authorRepository.findAll();
        assertEquals(4, before.size());
        assertNotNull(authorRepository.findById(pushkin.getId()));

        authorRepository.deleteById(pushkin.getId());
        entityManager.flush();

        List<Author> after = authorRepository.findAll();
        assertEquals(3, after.size());
        assertNull(authorRepository.findById(pushkin.getId()));
    }

}
