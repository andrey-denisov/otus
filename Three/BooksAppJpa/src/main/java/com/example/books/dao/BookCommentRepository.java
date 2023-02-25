package com.example.books.dao;

import com.example.books.model.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepository {
    Optional<BookComment> findById(long id);

    List<BookComment> findByBookId(long bookId);

    Optional<BookComment> create(BookComment comment);

    void deleteById(long id);
}
