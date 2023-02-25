package com.example.books.dao.impl;

import com.example.books.dao.BookCommentRepository;
import com.example.books.model.BookComment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public class BookCommentRepositoryJpa implements BookCommentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<BookComment> findById(long id) {
        return Optional.ofNullable(entityManager.find(BookComment.class, id));
    }

    @Override
    public List<BookComment> findByBookId(long bookId) {
        TypedQuery<BookComment> query = entityManager.createQuery("SELECT c FROM BookComment c WHERE book.id=:bookId", BookComment.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

    @Override
    public Optional<BookComment> create(BookComment comment) {
        entityManager.persist(comment);
        return Optional.ofNullable(comment);
    }

    @Override
    public void deleteById(long id) {
        Optional<BookComment> comment = findById(id);
        comment.ifPresent(bookComment -> entityManager.remove(bookComment));
    }

}
