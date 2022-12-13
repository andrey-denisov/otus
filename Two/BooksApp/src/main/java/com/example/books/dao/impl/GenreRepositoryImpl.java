package com.example.books.dao.impl;

import com.example.books.dao.GenreRepository;
import com.example.books.model.Genre;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Repository
public class GenreRepositoryImpl implements GenreRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public GenreRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAll() {
        final String SQL = "SELECT ID, NAME FROM GENRE";
        return jdbcTemplate.query(SQL, (rs, rowNum) -> new Genre(rs.getLong("id"), rs.getString("name")));
    }

    @Override
    public List<Genre> byBookId(long bookId) {
        final String SQL =
                "SELECT G.ID AS ID, G.NAME AS NAME " +
                        "FROM BOOK_GENRE BG " +
                        "INNER JOIN GENRE G ON G.ID = BG.GENRE_ID " +
                        "WHERE BG.BOOK_ID=:book_id";
        return jdbcTemplate.query(SQL, new HashMap<String, Long>() {{ put("book_id", bookId); }}, (rs, rowNum) -> new Genre(rs.getLong("ID"), rs.getString("NAME")));
    }

    @Override
    public Genre byId(long id) {
        final String SQL = "SELECT ID, NAME FROM GENRE WHERE ID=:id";
        try {
            return jdbcTemplate.queryForObject(SQL, new HashMap<String, Long>() {{
                put("id", id);
            }}, (rs, rowNum) -> new Genre(rs.getLong("id"), rs.getString("name")));
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        final String DELETE_SQL = "delete from Genre where id = :id";

        MapSqlParameterSource parameterSourceAuthor = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(DELETE_SQL, parameterSourceAuthor);

    }
}
