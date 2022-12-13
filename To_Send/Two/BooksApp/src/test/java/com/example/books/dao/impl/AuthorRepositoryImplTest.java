package com.example.books.dao.impl;

import com.example.books.dao.AuthorRepository;
import com.example.books.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
class AuthorRepositoryImplTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void getAll() {
        List<Author> result = authorRepository.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void byId() {
        Author result = authorRepository.findById(1L);
        assertEquals("Борис Акунин", result.getName());
    }

    @Test
    void createOk() {
        List<Author> before = authorRepository.findAll();
        assertEquals(2, before.size());
        authorRepository.create("Pushkin");
        List<Author> after = authorRepository.findAll();
        assertEquals(3, after.size());
    }

    @Test
    void createFail() {
        List<Author> before = authorRepository.findAll();
        assertEquals(2, before.size());
        authorRepository.create("Pushkin");
        List<Author> afterSuccess = authorRepository.findAll();
        assertEquals(3, afterSuccess.size());

        DuplicateKeyException thrown = assertThrows(DuplicateKeyException.class, () -> {
            authorRepository.create("Pushkin");
        }, "DuplicateKeyException was expected");

        List<Author> afterFail = authorRepository.findAll();
        assertEquals(3, afterFail.size());
    }

    @Test
    void deleteBydFail() {
        List<Author> before = authorRepository.findAll();
        assertEquals(2, before.size());
        DataIntegrityViolationException thrown = assertThrows(DataIntegrityViolationException.class, () -> {
            authorRepository.deleteById(1L);
        }, "DataIntegrityViolationException was expected");



        List<Author> after = authorRepository.findAll();
        assertEquals(2, after.size());
    }

    @Test
    void deleteBydOk() {
        Author pushkin = authorRepository.create("Pushkin");
        List<Author> before = authorRepository.findAll();
        assertEquals(3, before.size());
        assertNotNull(authorRepository.findById(pushkin.getId()));

        authorRepository.deleteById(pushkin.getId());

        List<Author> after = authorRepository.findAll();
        assertEquals(2, after.size());
        assertNull(authorRepository.findById(pushkin.getId()));
    }
}