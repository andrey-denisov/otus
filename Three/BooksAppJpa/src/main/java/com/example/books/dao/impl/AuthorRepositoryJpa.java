package com.example.books.dao.impl;

import com.example.books.dao.AuthorRepository;
import com.example.books.model.Author;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public class AuthorRepositoryJpa implements AuthorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Author> findAll() {
        TypedQuery<Author> query = entityManager.createQuery("SELECT a FROM Author a", Author.class);
        return query.getResultList();
    }

    public Optional<Author> findById(long id) {
        return Optional.ofNullable(entityManager.find(Author.class, id));
    }

    public Optional<Author> create(Author author) {
        entityManager.persist(author);
        return Optional.ofNullable(author);
    }

    public Author update(Author author) {
        entityManager.merge(author);
        return author;
    }

    public void deleteById(long id) {
        Optional<Author> author = findById(id);
        author.ifPresent(value -> entityManager.remove(value));
    }
}
