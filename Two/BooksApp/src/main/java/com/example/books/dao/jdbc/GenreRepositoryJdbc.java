package com.example.books.dao.jdbc;

import com.example.books.dao.GenreRepository;
import com.example.books.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class GenreRepositoryJdbc implements GenreRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        final String sql = "SELECT ID, NAME FROM GENRE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getLong("id"), new String(rs.getString("name").getBytes(), StandardCharsets.UTF_8)));
    }

    @Override
    public List<Genre> findByBookId(long bookId) {
        final String sql =
                "SELECT G.ID AS ID, G.NAME AS NAME " +
                        "FROM BOOK_GENRE BG " +
                        "INNER JOIN GENRE G ON G.ID = BG.GENRE_ID " +
                        "WHERE BG.BOOK_ID=:book_id";
        return jdbcTemplate.query(sql, new HashMap<String, Long>() {{ put("book_id", bookId); }}, (rs, rowNum) -> new Genre(rs.getLong("ID"), new String(rs.getString("NAME").getBytes(), StandardCharsets.UTF_8)));
    }

    @Override
    public Optional<Genre> findById(long id) {
        final String sql = "SELECT ID, NAME FROM GENRE WHERE ID=:id";
        try {
            Genre genre = jdbcTemplate.queryForObject(sql, new HashMap<String, Long>() {{
                put("id", id);
            }}, (rs, rowNum) -> new Genre(rs.getLong("id"), new String(rs.getString("name").getBytes(), StandardCharsets.UTF_8)));
            return Optional.ofNullable(genre);
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(long id) {
        final String deleteSql = "delete from Genre where id = :id";
        MapSqlParameterSource parameterSourceAuthor = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(deleteSql, parameterSourceAuthor);
    }
}
