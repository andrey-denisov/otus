package com.example.books.cli;

import com.example.books.model.Book;
import com.example.books.model.Genre;
import com.example.books.service.BookService;
import com.example.books.service.GenreService;
import com.example.books.util.BookFormatter;
import com.example.books.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellComponent;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@ShellComponent
public class BookShellCommand {

    private final Logger logger = LoggerFactory.getLogger(BookShellCommand.class);

    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;

    private final BookFormatter bookFormatter;

    public BookShellCommand(BookService bookService, AuthorService authorService, GenreService genreService, BookFormatter bookFormatter) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.bookFormatter = bookFormatter;
    }

    @ShellMethod(value = "List of books", key="list-books")
    public String bookList() {
        try {
            List<Book> bookList = bookService.findAll();
            if (null == bookList || bookList.isEmpty()) {
                return "No books found";
            }

            StringBuilder sb = new StringBuilder(" List of books:").append("\n");
            for (Book b : bookList) {
                sb.append(bookFormatter.format(b)).append("\n\n");
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
            Book book = bookService.findById(id);

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
    public String addBook(String title, String isbn, int issued, long authorId, String genreId ) {
        try {
            Book book = new Book();
            book.setTitle(title);
            book.setIssueYear(issued);
            book.setIsbn(isbn);
            book.setAuthor(authorService.findById(1L));

            Set<Genre> genres = new HashSet<>();
            String[] genreIdArray = genreId.split(",");
            for (String s : genreIdArray) {
                Genre genre = genreService.findById(Long.parseLong(s));
                if(null != genre) {
                    genres.add(genre);
                }
            }
            book.setGenres(genres);

            return new BookFormatter().format(bookService.add(book));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Невозможно добавить книгу: " + e.getMessage();
        }
    }


}
