package com.example.books.dao.impl;

import com.example.books.dao.AuthorRepository;
import com.example.books.model.Author;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Author> findAll() {
        TypedQuery<Author> query = entityManager.createQuery("SELECT a FROM Author a", Author.class);
        return query.getResultList();
    }

    public Author findById(long id) {
        return entityManager.find(Author.class, id);
    }

    @Transactional
    public Author create(Author author) {
        entityManager.persist(author);
        return author;
    }

    @Transactional
    public Author update(Author author) {
        entityManager.merge(author);
        return author;
    }

    @Transactional
    public Author deleteById(long id) {
        Author author = findById(id);
        if(null != author) {
            entityManager.remove(author);
        }
        return author;
    }

}
