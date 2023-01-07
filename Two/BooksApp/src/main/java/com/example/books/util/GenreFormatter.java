package com.example.books.util;

import com.example.books.model.Genre;
import org.springframework.stereotype.Component;
@Component
public class GenreFormatter implements Formatter<Genre> {
    @Override
    public String format(Genre value) {
        return String.format("id: %d, name: %s", value.getId(), value.getName());
    }
}
