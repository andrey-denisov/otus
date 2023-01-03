package com.example.books.dao.jdbc;

import com.example.books.dao.BookRepository;
import com.example.books.model.Author;
import com.example.books.model.Book;
import com.example.books.model.Genre;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BookRepositoryJdbc implements BookRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BookRepositoryJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Book> add(Book book) {
        final String insertBookSql = "insert into book (title, isbn, issue_year, author_id) values (:title, :isbn, :issue_year, :author_id)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("isbn", book.getIsbn())
                .addValue("issue_year", book.getIssueYear())
                .addValue("author_id", book.getAuthor().getId());

        this.jdbcTemplate.update(insertBookSql, parameterSource, keyHolder, new String[]{"id"});
        book.setId(keyHolder.getKey().longValue());
        insertGenresForBook(book.getId(), book.getGenres());
        return Optional.of(book);
    }

    @Override
    public void update(Book book) {
        final String bookUpdateSql = "UPDATE BOOK SET TITLE = :TITLE, ISBN = :ISBN, ISSUE_YEAR = :ISSUE_YEAR , AUTHOR_ID = :AUTOR_ID WHERE ID = :ID";

        // first update the book
        MapSqlParameterSource parameterSourceBook = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("isbn", book.getIsbn())
                .addValue("issue_year", book.getIssueYear())
                .addValue("author_id", book.getAuthor() == null ? null : book.getAuthor().getId());

        jdbcTemplate.update(bookUpdateSql, parameterSourceBook);

        // than replace genres
        deleteGenresForBook(book.getId());
        insertGenresForBook(book.getId(), book.getGenres());
    }

    private void deleteGenresForBook(long bookId) {
        final String deleteBookGenreLinks = "delete from book_genre where book_id = :book_id";
        MapSqlParameterSource parameterSourceBookGenre = new MapSqlParameterSource("book_id", bookId);
        jdbcTemplate.update(deleteBookGenreLinks, parameterSourceBookGenre);
    }

    private void insertGenresForBook(long bookId, Set<Genre> genres) {
        final String insertGenresOfBookSql = "insert into book_genre (book_id, genre_id) values (:book_id, :genre_id)";
        SqlParameterSource[] sqlParameterSources = new SqlParameterSource[genres.size()];
        genres.stream().map(g ->
                new MapSqlParameterSource()
                        .addValue("book_id", bookId)
                        .addValue("genre_id", g.getId())
        ).collect(Collectors.toList()).toArray(sqlParameterSources);

        jdbcTemplate.batchUpdate(insertGenresOfBookSql, sqlParameterSources);
    }

    @Override
    public void delete(long id) {
        final String deleteBookSql = "delete from book where id = :id";
        MapSqlParameterSource parameterSourceBook = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(deleteBookSql, parameterSourceBook);
    }

    @Override
    public List<Book> findAll() {
        final String sql =
                "SELECT B.ID, TITLE, ISBN,ISSUE_YEAR, A.ID as AUTHOR_ID, A.NAME AS AUTHOR_NAME, G.NAME AS GENRE_NAME, G.ID AS GENRE_ID " +
                  "FROM BOOK B " +
                  "INNER JOIN AUTHOR A ON B.AUTHOR_ID = A.ID " +
                  "INNER JOIN BOOK_GENRE BG ON B.ID = BG.BOOK_ID " +
                  "INNER JOIN GENRE G ON G.ID = BG.GENRE_ID ";

        return jdbcTemplate.query(sql, new BookResultSetExtractor());
    }

    @Override
    public Optional<Book> findById(long id) {
        final String sql =
                "SELECT B.ID, TITLE, ISBN,ISSUE_YEAR, A.ID as AUTHOR_ID, A.NAME AS AUTHOR_NAME, G.NAME AS GENRE_NAME, G.ID AS GENRE_ID " +
                        "FROM BOOK B " +
                        "JOIN AUTHOR A ON A.ID = B.AUTHOR_ID " +
                        "JOIN BOOK_GENRE BG ON B.ID = BG.BOOK_ID " +
                        "JOIN GENRE G ON G.ID = BG.GENRE_ID " +
                        "WHERE B.ID=:id";
        List<Book> result = jdbcTemplate.query(sql, new HashMap<String, Long>() {{
            put("id", id);
        }}, new BookResultSetExtractor());

        if(null == result || result.isEmpty()) {
            return Optional.empty();
        }

        if(result.size() > 1) {
            throw new IncorrectResultSizeDataAccessException("More that one book found by id=" + id, 1);
        }

        return Optional.of(result.get(0));
    }

    private static class BookResultSetExtractor implements ResultSetExtractor<List<Book>> {

        @Override
        public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Book> books = new HashMap<>();
            while (rs.next()) {
                long bookId = rs.getLong("id");
                Book book  = books.get(bookId);
                if (null == book) {
                    book = new Book();
                    book.setId(rs.getLong("id"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setTitle(new String(rs.getString("title").getBytes(), StandardCharsets.UTF_8));
                    book.setIssueYear(rs.getInt("issue_year"));
                    book.setAuthor(new Author(rs.getLong("author_id"), new String(rs.getString("author_name").getBytes(), StandardCharsets.UTF_8)));
                    books.put(bookId, book);
                }
                book.getGenres().add(new Genre(rs.getLong("genre_id"), new String(rs.getString("genre_name").getBytes(), StandardCharsets.UTF_8)));
            }
            return new ArrayList<>(books.values());
        }
    }
}
