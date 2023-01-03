package com.example.books.service;

import com.example.books.dao.GenreRepository;
import com.example.books.model.Genre;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    public List<Genre> findByBookId(long bookId) {
        return genreRepository.findByBookId(bookId);
    }

    public Genre findById(long id) {
        Optional<Genre> genre = genreRepository.findById(id);
        return genre.orElse(null);
    }

    public void deleteById(long id) {
        genreRepository.deleteById(id);
    }
}
