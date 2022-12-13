package com.example.books.dao.impl;

import com.example.books.dao.AuthorRepository;
import com.example.books.model.Author;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AuthorRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Author> findAll() {
        final String SQL = "SELECT ID, NAME FROM AUTHOR";
        return jdbcTemplate.query(SQL, (rs, rowNum) -> new Author(rs.getLong("id"), rs.getString("name")));
    }

    @Transactional
    @Override
    public Author create(String name) {
        final String SQL = "INSERT INTO AUTHOR (NAME) VALUES (:name)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("name", name);
        jdbcTemplate.update(SQL, parameterSource, keyHolder, new String[]{"id"});
        return new Author(keyHolder.getKey().longValue(), name);
    }

    @Override
    public Author findById(long id) {
        final String SQL = "SELECT ID, NAME FROM AUTHOR WHERE ID=:id";
        try {
            return jdbcTemplate.queryForObject(SQL, new HashMap<String, Long>() {{ put("id", id); }}, (rs, rowNum) -> new Author(rs.getLong("id"), rs.getString("name")));
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Author update(Author author) {
        return null;
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        final String DELETE_AUTHOR_SQL = "delete from Author where id = :id";

        MapSqlParameterSource parameterSourceAuthor = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(DELETE_AUTHOR_SQL, parameterSourceAuthor);
    }
}
