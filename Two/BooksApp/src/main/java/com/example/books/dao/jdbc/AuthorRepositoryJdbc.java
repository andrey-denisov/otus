package com.example.books.dao.jdbc;

import com.example.books.dao.AuthorRepository;
import com.example.books.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AuthorRepositoryJdbc implements AuthorRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Author> findAll() {
        final String sql = "SELECT ID, NAME FROM AUTHOR";
        return jdbcTemplate.query(sql, new AuthorRowMapper());
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
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Map.of("id", id), new AuthorRowMapper()));
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
        final String deleteAuthorSql = "DELETE FROM AUTHOR WHERE id = :id";
        MapSqlParameterSource parameterSourceAuthor = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(deleteAuthorSql, parameterSourceAuthor);
    }

    private static class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Author(rs.getLong("id"), new String(rs.getString("name").getBytes(), StandardCharsets.UTF_8));
        }
    }

}
