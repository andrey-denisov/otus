package com.example.books.util;

import com.example.books.model.Book;
import com.example.books.model.Genre;
import org.springframework.stereotype.Component;

@Component
public class BookFormatter implements Formatter<Book>{
    @Override
    public String format(Book value) {
        StringBuilder sb = new StringBuilder()
                .append("id:").append(value.getId()).append("\n")
                .append("Title:").append(value.getTitle()).append("\n")
                .append("ISBN:").append(value.getIsbn()).append("\n")
                .append("Author:").append(value.getAuthor()).append("\n")
                .append("Issued:").append(value.getIssueYear()).append("\n");

        sb.append("Genre:");
        for(Genre g : value.getGenres()) {
            sb.append(g);
        }
        return sb.toString();
    }
}
