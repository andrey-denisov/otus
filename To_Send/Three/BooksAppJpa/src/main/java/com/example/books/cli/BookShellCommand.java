package com.example.books.cli;

import com.example.books.dao.impl.AuthorRepositoryImpl;
import com.example.books.dao.impl.BookRepositoryImpl;
import com.example.books.dao.impl.GenreRepositoryImpl;
import com.example.books.model.Book;
import com.example.books.model.Genre;
import com.example.books.util.BookFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellComponent;

@ShellComponent
public class BookShellCommand {

    private final Logger logger = LoggerFactory.getLogger(BookShellCommand.class);

    private final BookRepositoryImpl bookRepository;
    private final AuthorRepositoryImpl authorRepository;
    private final com.example.books.dao.GenreRepository genreRepository;

    public BookShellCommand(BookRepositoryImpl bookRepository, AuthorRepositoryImpl authorRepository, GenreRepositoryImpl genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @ShellMethod(value = "List of books", key="list-books")
    public String bookList() {
        try {
            List<Book> bookList = bookRepository.findAll();
            if (null == bookList || bookList.isEmpty()) {
                return "No books found";
            }

            StringBuilder sb = new StringBuilder(" List of books:").append("\n");
            BookFormatter formatter = new BookFormatter();
            for (Book b : bookList) {
                sb.append(formatter.format(b)).append("\n\n");
            }

            return sb.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Cannot obtain a list of books:" + e.getMessage();
        }
    }

    @ShellMethod(value = "Book by id", key="book-by-id")
    public String bookByID(long id) {
        try {
            Book book = bookRepository.findById(id);

            if (book == null) {
                return MessageFormat.format("Book with id={0} not found", id);
            }

            return new BookFormatter().format(book);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Cannot obtain a book with id=" + id + ": " + e.getMessage();
        }
    }

    @ShellMethod(value = "Add a books", key = "add-book")
    public String bookList(String title, String isbn, int issued, long authorId, String genreId) throws SQLException {
        try {
            Book book = new Book();
            book.setTitle(title);
            book.setIssueYear(issued);
            book.setIsbn(isbn);
            book.setAuthor(authorRepository.findById(1L));

            Set<Genre> genres = new HashSet<>();
            String[] genreIdArray = genreId.split(",");
            for (int i = 0; i < genreIdArray.length; i++) {
                Genre genre = genreRepository.findById(Long.parseLong(genreIdArray[i]));
                genres.add(genre);
            }
            book.setGenres(genres);

            return new BookFormatter().format(bookRepository.create(book));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Cannot obtain add a new book: " + e.getMessage();
        }
    }
}
