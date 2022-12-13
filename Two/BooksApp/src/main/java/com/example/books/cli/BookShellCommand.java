package com.example.books.cli;

import com.example.books.dao.AuthorRepository;
import com.example.books.dao.BookRepository;
import com.example.books.dao.GenreRepository;
import com.example.books.model.Book;
import com.example.books.model.Genre;
import com.example.books.util.BookFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellComponent;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ShellComponent
public class BookShellCommand {

    private final Logger logger = LoggerFactory.getLogger(BookShellCommand.class);

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookShellCommand(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @ShellMethod(value = "List of books", key="list-books")
    public String bookList() {
        try {
            List<Book> bookList = bookRepository.getAll();
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
            return "Невозможно получить список книг: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Book by id", key="book-by-id")
    public String bookByID(long id) {
        try {
            Book book = bookRepository.byId(id);

            if (book == null) {
                return MessageFormat.format("Book with id={0} not found", id);
            }

            return new BookFormatter().format(book);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Невозможно получить книгу по id: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Add a books", key="add-book")
    public String addBook(String title, String isbn, int issued, long authorId, String genreId ) throws SQLException {
        try {
            Book book = new Book();
            book.setTitle(title);
            book.setIssueYear(issued);
            book.setIsbn(isbn);
            book.setAuthor(authorRepository.findById(1L));

            Set<Genre> genres = new HashSet<>();
            String[] genreIdArray = genreId.split(",");
            for (int i = 0; i < genreIdArray.length; i++) {
                Genre genre = genreRepository.byId(Long.parseLong(genreIdArray[i]));
                genres.add(genre);
            }
            book.setGenres(genres);

            return new BookFormatter().format(bookRepository.add(book));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Невозможно добавить книгу: " + e.getMessage();
        }
    }


}
