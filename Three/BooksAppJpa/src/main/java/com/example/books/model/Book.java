package com.example.books.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = Author.class, orphanRemoval = false)
    @JoinColumn(name="author_id")
    private Author author;
    private String title;
    private int issueYear;
    private String isbn;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable (
            name="book_genre",
            joinColumns={ @JoinColumn(name="book_id", referencedColumnName="id") },
            inverseJoinColumns={ @JoinColumn(name="genre_id", referencedColumnName="id", unique=true) },
            foreignKey = @ForeignKey(name = "")
    )
    private Set<Genre> genres = new HashSet<>();

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIssueYear() {
        return issueYear;
    }

    public void setIssueYear(int issueYear) {
        this.issueYear = issueYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return issueYear == book.issueYear &&
                Objects.equals(id, book.id) &&
                Objects.equals(author, book.author) &&
                Objects.equals(title, book.title) &&
                Objects.equals(isbn, book.isbn) &&
                Objects.equals(genres, book.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, title, issueYear, isbn, genres);
    }

    @Override
    public String toString() {
        String g = genres != null ? genres.toString() : "no genres";
        String a = author != null ? author.toString() : "no author";
        return (
                "Book{" +
                        "id=" +
                        id +
                        ", genres=" + g +
                        ", author=" + a +
                        ", title='" +
                        title +
                        '\'' +
                        ", issueYear=" +
                        issueYear +
                        ", isbn='" +
                        isbn +
                        '\'' +
                        '}'
        );
    }
}
