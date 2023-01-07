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
        List<Author> result = authorRepository.findAll();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void shouldReturnAuthorById() {
        Optional<Author> result = authorRepository.findById(1L);
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Борис Акунин");
    }

    @Test
    void shouldAddAuthorToDatabase() {
        List<Author> before = authorRepository.findAll();
        assertThat(before.size()).isEqualTo(2);
        authorRepository.add("Pushkin");
        List<Author> after = authorRepository.findAll();
        assertThat(after.size()).isEqualTo(3);
    }

    @Test
    void shouldFailWhenTryingToAddAuthorBecauseOfKeyDuplication() {
        List<Author> before = authorRepository.findAll();
        assertThat(before.size()).isEqualTo(2);
        authorRepository.add("Pushkin");
        List<Author> afterSuccess = authorRepository.findAll();
        assertThat(afterSuccess.size()).isEqualTo(3);
        assertThatThrownBy(() -> authorRepository.add("Pushkin")).isInstanceOf(DuplicateKeyException.class);
        List<Author> afterFail = authorRepository.findAll();
        assertThat(afterFail.size()).isEqualTo(3);
    }

    @Test
    void shouldFailWhenDeletingAuthorByIdBecauseIntegrityViolation() {
        List<Author> before = authorRepository.findAll();
        assertThat(before.size()).isEqualTo(2);
        assertThatThrownBy(() -> authorRepository.deleteById(1L)).isInstanceOf(DataIntegrityViolationException.class);
        List<Author> after = authorRepository.findAll();
        assertThat(after.size()).isEqualTo(2);
    }

    @Test
    void shouldDeleteAuthorById() {
        Optional<Author> pushkin = authorRepository.add("Pushkin");
        List<Author> before = authorRepository.findAll();
        assertThat(pushkin).isPresent();
        assertThat(before.size()).isEqualTo(3);
        assertThat(authorRepository.findById(pushkin.get().getId())).isNotNull();

        authorRepository.deleteById(pushkin.get().getId());

        List<Author> after = authorRepository.findAll();
        assertThat(after.size()).isEqualTo(2);
        assertThat(authorRepository.findById(pushkin.get().getId()).isPresent()).isFalse();
    }
}