package com.example.books.cli;

import com.example.books.service.BookPresentationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellComponent;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@ShellComponent
public class BookShellCommand {

    private final Logger logger = LoggerFactory.getLogger(BookShellCommand.class);

    private final BookPresentationService bookPresentationService;

    @ShellMethod(value = "List of books", key="list-books")
    public String bookList() {
        try {
            return bookPresentationService.listAllBooks();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Cannot obtain a list of books:" + e.getMessage();
        }
    }

    @ShellMethod(value = "Book by id", key="book-by-id")
    public String bookByID(long id) {
        try {
            return bookPresentationService.bookByID(id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Cannot obtain a book with id=" + id + ": " + e.getMessage();
        }
    }

    @ShellMethod(value = "Add a books", key = "add-book")
    public String addBook(String title, String isbn, int issued, long authorId, String genreId) {
        try {
            return bookPresentationService.addBook(title, isbn, issued, authorId, genreId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Cannot add a new book: " + e.getMessage();
        }
    }
}
