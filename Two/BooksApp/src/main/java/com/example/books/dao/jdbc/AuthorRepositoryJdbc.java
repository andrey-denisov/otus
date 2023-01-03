package com.example.books.dao.jdbc;

import com.example.books.dao.AuthorRepository;
import com.example.books.model.Author;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryJdbc implements AuthorRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AuthorRepositoryJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Author> findAll() {
        final String sql = "SELECT ID, NAME FROM AUTHOR";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Author(rs.getLong("id"), rs.getString("name")));
    }

    @Override
    public Optional<Author> add(String name) {
        final String sql = "INSERT INTO AUTHOR (NAME) VALUES (:name)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("name", name);
        jdbcTemplate.update(sql, parameterSource, keyHolder, new String[]{"id"});
        return Optional.of(new Author(keyHolder.getKey().longValue(), name));
    }

    @Override
    public Optional<Author> findById(long id) {
        final String sql = "SELECT ID, NAME FROM AUTHOR WHERE ID=:id";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, new HashMap<String, Long>() {{ put("id", id); }}, (rs, rowNum) -> new Author(rs.getLong("id"), new String(rs.getString("name").getBytes(), StandardCharsets.UTF_8))));
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Author> update(Author author) {
        final String sql = "UPDATE AUTHOR SET name=:name where id=:id";
        MapSqlParameterSource parameterSourceAuthor = new MapSqlParameterSource("id", author.getId());
        parameterSourceAuthor.addValue("name", author.getName());
        jdbcTemplate.update(sql, parameterSourceAuthor);
        return Optional.of(author);
    }

    @Override
    public void deleteById(long id) {
        final String deleteAuthorSql = "delete from Author where id = :id";
        MapSqlParameterSource parameterSourceAuthor = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(deleteAuthorSql, parameterSourceAuthor);
    }
}
