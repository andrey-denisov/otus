package com.example.books.util;

import com.example.books.model.Author;
import com.example.books.model.Book;
import com.example.books.model.Genre;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class BookFormatterTest {

    @Test
    void formatCompletelyFilledOut() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Название");
        book.setIsbn("12345");
        book.setIssueYear(1990);
        book.setAuthor(new Author(1, "Автор"));
        book.setGenres(new HashSet<Genre>() {{
            add(new Genre(1, "Жанр 1"));
            add(new Genre(2, "Жанр 2"));
        }});

        String formatted = new BookFormatter(new AuthorFormatter(), new GenreFormatter()).format(book);
        assertEquals("------ Book ------\nid: 1\n" +
                "Title: Название\n" +
                "ISBN: 12345\n" +
                "Author: id: 1, name: Автор\n" +
                "Issued: 1990\n" +
                "Genres: [id: 1, name: Жанр 1; id: 2, name: Жанр 2]", formatted);
    }
}