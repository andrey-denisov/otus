package com.example.books.dao.impl;

import com.example.books.dao.BookCommentRepository;
import com.example.books.model.Book;
import com.example.books.model.BookComment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("unused")
@DataJpaTest
@Import({BookCommentRepositoryJpa.class})
class BookCommentRepositoryTest {

    @Autowired
    private BookCommentRepository bookCommentRepository;

    @Test
    void shouldReturnCommentById() {
        final long commentId = 1L;
        Optional<BookComment> result = bookCommentRepository.findById(commentId);
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(commentId);
    }

    @Test
    void shouldReturnCommentsByBookId() {
        final long bookId = 3L;
        List<BookComment> result = bookCommentRepository.findByBookId(bookId);
        assertThat(result).hasSize(1);
    }

    @Test
    void shouldCreateComment() {
        final long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        Optional<BookComment> result = bookCommentRepository.create(new BookComment(book, "Very good book"));
        assertThat(result).isPresent();
        Optional<BookComment> found = bookCommentRepository.findById(result.get().getId());
        assertThat(found).isPresent();
        assertThat(result.get()).isEqualTo(found.get());
    }

    @Test
    void shouldDeleteCommentById() {
        final long bookId = 1L;
        final long commentId = 1L;
        bookCommentRepository.deleteById(bookId);
        Optional<BookComment> found = bookCommentRepository.findById(commentId);
        assertThat(found).isNotPresent();
    }
}