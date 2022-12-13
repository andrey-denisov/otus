package com.example.books.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class BookComment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    private long bookId;
    private String comment;

    protected BookComment() {
    }

    public BookComment(long bookId, String comment) {
        this.bookId = bookId;
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public long getBookId() {
        return bookId;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookComment comment1 = (BookComment) o;
        return id == comment1.id &&
                bookId == comment1.bookId &&
                Objects.equals(comment, comment1.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookId, comment);
    }
}
