package com.example.books.util;

import com.example.books.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorFormatter implements Formatter<Author> {
    @Override
    public String format(Author value) {
        return new StringBuilder()
                .append("id:").append(value.getId()).append(", ")
                .append("name:").append(value.getName()).append("\n")
                .toString();
    }
}
