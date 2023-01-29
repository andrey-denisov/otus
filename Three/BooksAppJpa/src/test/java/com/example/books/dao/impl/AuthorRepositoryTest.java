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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("unused")
@DataJpaTest
@ComponentScan
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void shouldReturnAllAuthors() {
        final int authorsAmount = 3;
        List<Author> result = authorRepository.findAll();
        assertThat(result).hasSize(authorsAmount);
    }

    @Test
    public void shouldReturnAuthorById() {
        final long authorId = 1L;
        Optional<Author>  result = authorRepository.findById(authorId);
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(authorId);
    }

    @Test
    public void shouldCreateAuthor() {
        Author author = new Author("Panaev");
        Optional<Author> result = authorRepository.create(author);
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isNotEqualTo(0);

        Optional<Author> newOne = authorRepository.findById(result.get().getId());
        assertThat(newOne).isPresent();
        assertThat(author.getName()).isEqualTo(newOne.get().getName());
    }

    @Test
    public void shouldUpdateAuthor() {
        final String newName = "Skubichevskiy";
        Optional<Author> author = authorRepository.findById(1L);
        assertThat(author).isPresent();
        assertThat(newName).isNotEqualTo(author.get().getName());

        author.get().setName(newName);
        Author result = authorRepository.update(author.get());

        Optional<Author> newOne = authorRepository.findById(result.getId());
        assertThat(newOne).isPresent();
        assertThat(author.get().getId()).isEqualTo(newOne.get().getId());
        assertThat(author.get().getName()).isEqualTo(newOne.get().getName());
    }

    @Test
    void shouldFailWhenTryingToDeleteAuthorBydI() {
        final int authorsAmount = 3;
        final long authorId = 1L;
        List<Author> before = authorRepository.findAll();
        assertThat(before).hasSize(authorsAmount);
        assertThatThrownBy(() -> {
                    authorRepository.deleteById(1L);
                    entityManager.flush();
                }, "PersistenceException was expected"
        ).isInstanceOf(PersistenceException.class);
    }

    @Test
    void shouldDeleteAuthorByd() {
        final int initialAuthorsAmount = 4;
        final int finalAuthorsAmount = 3;
        Optional<Author> pushkin = authorRepository.create(new Author("Pushkin"));
        assertThat(pushkin).isPresent();
        List<Author> before = authorRepository.findAll();
        assertThat(before).hasSize(initialAuthorsAmount);
        assertThat(authorRepository.findById(pushkin.get().getId())).isPresent();

        authorRepository.deleteById(pushkin.get().getId());
        entityManager.flush();

        List<Author> after = authorRepository.findAll();
        assertThat(after).hasSize(finalAuthorsAmount);
        assertThat(authorRepository.findById(pushkin.get().getId())).isNotPresent();
    }
}
