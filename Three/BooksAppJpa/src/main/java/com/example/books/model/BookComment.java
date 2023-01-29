package com.example.books.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@SuppressWarnings("unused")
@Entity
public class BookComment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @ManyToOne
    private Book book;
    private String comment;

    protected BookComment() {
    }

    public BookComment(Book book, String comment) {
        this.book = book;
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public Book getBook() {
        return book;
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
                book == comment1.book &&
                Objects.equals(comment, comment1.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, comment);
    }
}
