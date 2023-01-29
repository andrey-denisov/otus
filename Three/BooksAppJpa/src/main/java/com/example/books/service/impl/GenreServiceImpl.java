package com.example.books.service.impl;

import com.example.books.dao.GenreRepository;
import com.example.books.model.Genre;
import com.example.books.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Set<Genre> findByBookId(long bookId) {
        return genreRepository.findByBookId(bookId);
    }

    @Override
    public Genre findById(long id) {
        Optional<Genre> genre = genreRepository.findById(id);
        return genre.orElse(null);
    }

    @Override
    public void deleteById(long id) {
        genreRepository.deleteById(id);
    }
}
