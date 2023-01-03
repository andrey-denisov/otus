package com.example.books.service.impl;

import com.example.books.dao.AuthorRepository;
import com.example.books.model.Author;
import com.example.books.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author findById(long id) {
        Optional<Author> author = authorRepository.findById(id);
        return author.orElse(null);
    }

}
