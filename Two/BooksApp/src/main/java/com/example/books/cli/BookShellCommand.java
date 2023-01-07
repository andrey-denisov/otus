package com.example.books.cli;

import com.example.books.model.Book;
import com.example.books.model.Genre;
import com.example.books.service.BookService;
import com.example.books.service.impl.GenreServiceImpl;
import com.example.books.util.BookFormatter;
import com.example.books.service.AuthorService;
import com.example.books.util.ListFormatter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@ShellComponent
public class BookShellCommand {

    private final Logger logger = LoggerFactory.getLogger(BookShellCommand.class);
    private final BookService bookService;
    private final GenreServiceImpl genreService;
    private final AuthorService authorService;
    private final BookFormatter bookFormatter;

    @ShellMethod(value = "List of books", key="list-books")
    public String bookList() {
        try {
            List<Book> bookList = bookService.findAll();
            if (CollectionUtils.isEmpty(bookList)) {
                return "No books found";
            }

            return ListFormatter.format(bookList, bookFormatter, "\n");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Cannot obtain list of books: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Book by id", key="book-by-id")
    public String bookByID(long id) {
        try {
            Book book = bookService.findById(id);
            if (Objects.isNull(book)) {
                return String.format("Book with id=%d not found", id);
            }
            return bookFormatter.format(book);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Cannot get book by id: " + e.getMessage();
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
                if(Objects.nonNull(genre)) {
                    genres.add(genre);
                }
            }
            book.setGenres(genres);

            return bookFormatter.format(bookService.add(book));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Cannot add a book: " + e.getMessage();
        }
    }


}
