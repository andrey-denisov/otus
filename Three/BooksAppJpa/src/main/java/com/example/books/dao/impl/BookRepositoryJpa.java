package com.example.books.dao.impl;

import com.example.books.dao.BookRepository;
import com.example.books.model.Book;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b", Book.class);
        return query.getResultList();

    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    public Optional<Book> create(Book book) {
        entityManager.persist(book);
        return Optional.ofNullable(book);
    }

    @Override
    public void deleteById(long id) {
        Optional<Book> book = findById(id);
        book.ifPresent(value -> entityManager.remove(value));
    }

    @Override
    public Book update(Book book) {
        entityManager.merge(book);
        return book;
    }

}
