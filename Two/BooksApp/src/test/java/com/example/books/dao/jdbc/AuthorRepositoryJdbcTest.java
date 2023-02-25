package com.example.books.dao.jdbc;

import com.example.books.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthorRepositoryJdbc.class))
class AuthorRepositoryJdbcTest {

    @Autowired
    private AuthorRepositoryJdbc authorRepository;

    @Test
    void shouldReturnAllAuthorsList() {
        final int initialAmount = 2;
        List<Author> result = authorRepository.findAll();
        assertThat(result.size()).isEqualTo(initialAmount);
    }

    @Test
    void shouldReturnAuthorById() {
        final long akuninId = 1L;
        Optional<Author> result = authorRepository.findById(akuninId);
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Борис Акунин");
    }

    @Test
    void shouldAddAuthorToDatabase() {
        final int initialAmount = 2;
        final int finalAmount = 3;
        final String newAuthorName = "Pushkin";
        List<Author> before = authorRepository.findAll();
        assertThat(before.size()).isEqualTo(initialAmount);
        authorRepository.add(newAuthorName);
        List<Author> after = authorRepository.findAll();
        assertThat(after.size()).isEqualTo(finalAmount);
    }

    @Test
    void shouldFailWhenTryingToAddAuthorBecauseOfKeyDuplication() {
        final int initialAmount = 2;
        final int finalAmount = 3;
        final String newAuthorName = "Pushkin";
        List<Author> before = authorRepository.findAll();
        assertThat(before.size()).isEqualTo(initialAmount);
        authorRepository.add(newAuthorName);
        List<Author> afterSuccess = authorRepository.findAll();
        assertThat(afterSuccess.size()).isEqualTo(finalAmount);
        assertThatThrownBy(() -> authorRepository.add(newAuthorName)).isInstanceOf(DuplicateKeyException.class);
        List<Author> afterFail = authorRepository.findAll();
        assertThat(afterFail.size()).isEqualTo(finalAmount);
    }

    @Test
    void shouldFailWhenDeletingAuthorByIdBecauseIntegrityViolation() {
        final int initialAmount = 2;
        final long akuninId = 1L;
        List<Author> before = authorRepository.findAll();
        assertThat(before.size()).isEqualTo(initialAmount);
        assertThatThrownBy(() -> authorRepository.deleteById(akuninId)).isInstanceOf(DataIntegrityViolationException.class);
        List<Author> after = authorRepository.findAll();
        assertThat(after.size()).isEqualTo(initialAmount);
    }

    @Test
    void shouldDeleteAuthorById() {
        final int initialAmount = 2;
        final int finalAmount = 3;
        final String newAuthorName = "Pushkin";
        Optional<Author> pushkin = authorRepository.add(newAuthorName);
        List<Author> before = authorRepository.findAll();
        assertThat(pushkin).isPresent();
        assertThat(before.size()).isEqualTo(finalAmount);
        assertThat(authorRepository.findById(pushkin.get().getId())).isNotNull();

        authorRepository.deleteById(pushkin.get().getId());

        List<Author> after = authorRepository.findAll();
        assertThat(after.size()).isEqualTo(initialAmount);
        assertThat(authorRepository.findById(pushkin.get().getId()).isPresent()).isFalse();
    }
}