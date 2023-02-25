package com.example.books.dao.impl;

import com.example.books.dao.GenreRepository;
import com.example.books.model.Genre;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unused")
@Repository
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = entityManager.createQuery("SELECT g FROM Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Optional<Genre> findById(long id) {
        Genre genre = entityManager.find(Genre.class, id);
        return Optional.ofNullable(genre);
    }

    @Override
    public Set<Genre> findByBookId(long bookId) {
        TypedQuery<Genre> query = entityManager.createQuery("SELECT g FROM Book b INNER JOIN b.genres g WHERE b.id=:bookId", Genre.class);
        query.setParameter("bookId", bookId);
        return new HashSet<>(query.getResultList());
    }

    @Override
    public Optional<Genre> create(Genre genre) {
        entityManager.persist(genre);
        return Optional.ofNullable(genre);
    }

    @Override
    public Genre update(Genre genre) {
        entityManager.merge(genre);
        return genre;
    }

    @Override
    public void deleteById(long id) {
        Optional<Genre> genre = findById(id);
        genre.ifPresent(value -> entityManager.remove(value));
    }

}
