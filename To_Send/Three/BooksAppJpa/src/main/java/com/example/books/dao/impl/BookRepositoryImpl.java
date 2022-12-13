package com.example.books.dao.impl;

import com.example.books.dao.BookRepository;
import com.example.books.model.Book;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b", Book.class);
        return query.getResultList();

    }

    @Override
    public Book findById(long id) {
        return entityManager.find(Book.class, id);
    }

    @Override
    @Transactional
    public Book create(Book book) {
        entityManager.persist(book);
        return book;
    }

    @Override
    @Transactional
    public Book deleteById(long id) {
        Book book = findById(id);
        if(null != book) {
            entityManager.remove(book);
        }
        return book;
    }

    @Override
    @Transactional
    public Book update(Book book) {
        entityManager.merge(book);
        return book;
    }

}
