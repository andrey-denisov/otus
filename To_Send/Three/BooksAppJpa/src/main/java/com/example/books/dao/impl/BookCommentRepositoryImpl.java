package com.example.books.dao.impl;

import com.example.books.dao.BookCommentRepository;
import com.example.books.model.BookComment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BookCommentRepositoryImpl implements BookCommentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BookComment findById(long id) {
        return entityManager.find(BookComment.class, id);
    }

    @Override
    public List<BookComment> findByBookId(long bookId) {
        TypedQuery<BookComment> query = entityManager.createQuery("SELECT c FROM BookComment c WHERE c.bookId=:bookId", BookComment.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

    @Override
    @Transactional
    public BookComment create(BookComment comment) {
        entityManager.persist(comment);
        return comment;
    }

    @Override
    @Transactional
    public BookComment deleteById(long id) {
        BookComment comment = findById(id);
        if(null != comment) {
            entityManager.remove(comment);
        }
        return comment;
    }

}
