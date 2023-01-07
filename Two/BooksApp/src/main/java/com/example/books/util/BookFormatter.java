package com.example.books.util;

import com.example.books.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class BookFormatter implements Formatter<Book>{
    private final AuthorFormatter authorFormatter;
    private final GenreFormatter genreFormatter;

    @Override
    public String format(Book value) {
        return String.format(
                "------ Book ------\nid: %s\nTitle: %s\nISBN: %s\nAuthor: %s\nIssued: %s\nGenres: [%s]",
                value.getId(),
                value.getTitle(),
                value.getIsbn(),
                authorFormatter.format(value.getAuthor()),
                value.getIssueYear(),
                ListFormatter.format(new ArrayList<>(value.getGenres()), genreFormatter, "; ")
                );
    }
}
