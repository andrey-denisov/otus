package com.example.books.dao;

import com.example.books.model.BookComment;

import javax.transaction.Transactional;
import java.util.List;

public interface BookCommentRepository {
    BookComment findById(long id);

    List<BookComment> findByBookId(long bookId);

    @Transactional
    BookComment create(BookComment comment);

    @Transactional
    BookComment deleteById(long id);
}
