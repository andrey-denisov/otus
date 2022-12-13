package com.example.books.dao.impl;

import com.example.books.model.BookComment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
class BookCommentRepositoryTest {

    @Autowired
    private BookCommentRepositoryImpl bookCommentRepository;


    @Test
    void findById() {
        BookComment result = bookCommentRepository.findById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void findByBookId() {
        List<BookComment> result = bookCommentRepository.findByBookId(3L);
        assertEquals(1L, result.size());
    }

    @Test
    void create() {
        BookComment result = bookCommentRepository.create(new BookComment(1L, "Very good book"));
        BookComment found = bookCommentRepository.findById(result.getId());
        assertEquals(result, found);
    }

    @Test
    void deleteById() {
        BookComment deleted = bookCommentRepository.deleteById(1L);
        assertNotNull(deleted);
        BookComment found = bookCommentRepository.findById(1L);
        assertNull(found);
    }
}