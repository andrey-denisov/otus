package com.example.books.model;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@SuppressWarnings("unused")
@BatchSize(size=25)
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    private String name;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private Set<Book> books;
    protected Author() {
    }

    public Author(String name) {
        this.name = name;
    }

    public Author(long id, String name) {
        this.id = id;
        this.name = name;
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Author{id=%d, name='%s'}", id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id == author.id &&
                Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
