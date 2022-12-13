package com.example.books.dao.impl;

import com.example.books.dao.GenreRepository;
import com.example.books.model.Genre;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class GenreRepositoryImpl implements GenreRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = entityManager.createQuery("SELECT g FROM Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Genre findById(long id) {
        return entityManager.find(Genre.class, id);
    }

    @Override
    public Set<Genre> findByBookId(long bookId) {
        Query nativeQuery = entityManager.createNativeQuery("SELECT G.ID AS ID, NAME FROM GENRE G INNER JOIN BOOK_GENRE BG ON G.ID = BG.GENRE_ID WHERE BG.BOOK_ID=:bookId", Genre.class);
        nativeQuery.setParameter("bookId", bookId);
        return new HashSet<>(nativeQuery.getResultList());
    }

    @Override
    @Transactional
    public Genre create(Genre genre) {
        entityManager.persist(genre);
        return genre;
    }

    @Override
    @Transactional
    public Genre update(Genre genre) {
        entityManager.merge(genre);
        return genre;
    }

    @Override
    @Transactional
    public Genre deleteById(long id) {
        Genre genre = findById(id);
        if(null != genre) {
            entityManager.remove(genre);
        }
        return genre;
    }

}
