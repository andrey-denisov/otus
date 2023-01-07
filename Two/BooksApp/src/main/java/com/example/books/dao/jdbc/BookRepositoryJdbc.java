package com.example.books.dao.jdbc;

import com.example.books.dao.BookRepository;
import com.example.books.dao.GenreRepository;
import com.example.books.model.Author;
import com.example.books.model.Book;
import com.example.books.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class BookRepositoryJdbc implements BookRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> add(Book book) {
        final String insertBookSql = "INSERT INTO BOOK (title, isbn, issue_year, author_id) VALUES (:title, :isbn, :issue_year, :author_id)";

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
        final String bookUpdateSql = "UPDATE BOOK SET title = :title, isbn = :isbn, issue_year = :issueYear , author_id = :authorId WHERE id = :id";

        // first update the book
        MapSqlParameterSource parameterSourceBook = new MapSqlParameterSource()
                .addValue("id", book.getId())
                .addValue("title", book.getTitle())
                .addValue("isbn", book.getIsbn())
                .addValue("issueYear", book.getIssueYear())
                .addValue("authorId", book.getAuthor() == null ? null : book.getAuthor().getId());

        jdbcTemplate.update(bookUpdateSql, parameterSourceBook);

        // than replace genres
        deleteGenresForBook(book.getId());
        insertGenresForBook(book.getId(), book.getGenres());
    }

    private void deleteGenresForBook(long bookId) {
        final String deleteBookGenreLinks = "DELETE FROM BOOK_GENRE WHERE book_id = :bookId";
        MapSqlParameterSource parameterSourceBookGenre = new MapSqlParameterSource("bookId", bookId);
        jdbcTemplate.update(deleteBookGenreLinks, parameterSourceBookGenre);
    }

    private void insertGenresForBook(long bookId, Set<Genre> genres) {
        final String insertGenresOfBookSql = "INSERT INTO BOOK_GENRE (book_id, genre_id) VALUES (:bookId, :genreId)";
        SqlParameterSource[] sqlParameterSources = new SqlParameterSource[genres.size()];
        genres.stream().map(g ->
                new MapSqlParameterSource()
                        .addValue("bookId", bookId)
                        .addValue("genreId", g.getId())
        ).collect(Collectors.toList()).toArray(sqlParameterSources);

        jdbcTemplate.batchUpdate(insertGenresOfBookSql, sqlParameterSources);
    }

    @Override
    public void delete(long id) {
        final String deleteBookSql = "DELETE FROM BOOK WHERE id = :id";
        MapSqlParameterSource parameterSourceBook = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(deleteBookSql, parameterSourceBook);
    }

    @Override
    public List<Book> findAll() {
        final String sql =
                "SELECT B.ID, TITLE, ISBN,ISSUE_YEAR, A.ID as AUTHOR_ID, A.NAME AS AUTHOR_NAME " +
                        "FROM BOOK B " +
                        "INNER JOIN AUTHOR A ON B.AUTHOR_ID = A.ID ";

        List<Book> books = jdbcTemplate.query(sql, new BooksRowMapper());

        Map<Long, Genre> genresById = genreRepository.findAll().stream().collect(Collectors.toMap(Genre::getId, Function.identity()));
        Map<Long, Book> booksById = books.stream().collect(Collectors.toMap(Book::getId, Function.identity()));
        List<BookGenreRelation> relations = findAllBookGenreRelations();
        relations.forEach(relation -> {
            Book book = booksById.get(relation.getBookId());
            Genre genre = genresById.get(relation.getGenreId());
            if (Objects.isNull(book) || Objects.isNull(genre)) {
                throw new RuntimeException(String.format("Bad relation. BookId: %d, genreId: %d",
                        relation.getBookId(), relation.getGenreId()));
            }
            book.getGenres().add(genre);
        });

        return books;
    }

    @Override
    public Optional<Book> findById(long id) {
        final String sql =
                "SELECT b.id, title, isbn, issue_year, a.id as author_id, a.name as author_name, g.name as genre_name, g.id as genre_id " +
                        "FROM BOOK b " +
                        "JOIN AUTHOR a ON a.id = b.author_id " +
                        "JOIN BOOK_GENRE bg ON b.ID = bg.book_id " +
                        "JOIN GENRE g ON g.id = bg.genre_id " +
                        "WHERE b.id=:id";
        List<Book> result = jdbcTemplate.query(sql, Map.of("id", id), new BookResultSetExtractor());

        if(CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }

        if(result.size() > 1) {
            throw new IncorrectResultSizeDataAccessException("More that one book found by id=" + id, 1);
        }

        return Optional.of(result.get(0));
    }

    private static class BooksRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(rs.getLong("id"),
                    new Author(rs.getLong("author_id"), rs.getString("author_name")),
                    rs.getString("title"),
                    rs.getInt("issue_year"),
                    rs.getString("isbn"),
                    new HashSet<>()
            );
        }
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

    private List<BookGenreRelation> findAllBookGenreRelations() {
        final String sql = "SELECT BOOK_ID, GENRE_ID FROM BOOK_GENRE";
        return jdbcTemplate.query(sql, new BookGenreRelationMapper());
    }

    private static class BookGenreRelationMapper implements RowMapper<BookGenreRelation> {
        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BookGenreRelation(rs.getLong("book_id"), rs.getLong("genre_id"));
        }
    }

    @Data
    @AllArgsConstructor
    private static class BookGenreRelation {
        private long bookId;
        private long genreId;
    }
}
