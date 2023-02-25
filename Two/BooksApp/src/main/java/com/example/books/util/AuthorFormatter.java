package com.example.books.util;

import com.example.books.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorFormatter implements Formatter<Author> {
    @Override
    public String format(Author value) {
        return String.format("id: %d, name: %s", value.getId(), value.getName());
    }
}
