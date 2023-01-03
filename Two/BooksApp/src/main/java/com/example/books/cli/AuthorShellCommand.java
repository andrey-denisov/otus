package com.example.books.cli;

import com.example.books.model.Author;
import com.example.books.util.AuthorFormatter;
import com.example.books.service.AuthorService;
import org.springframework.shell.standard.ShellComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@ShellComponent
public class AuthorShellCommand {

    private final Logger logger = LoggerFactory.getLogger(AuthorShellCommand.class);

    private final AuthorService authorService;
    private final AuthorFormatter authorFormatter;

    public AuthorShellCommand(AuthorService authorService, AuthorFormatter authorFormatter) {
        this.authorService = authorService;
        this.authorFormatter = authorFormatter;
    }

    @ShellMethod(value = "list authors", key="list-authors")
    public String listAuthors() {
        try {
            List<Author> authorList = authorService.findAll();

            if (null == authorList || authorList.isEmpty()) {
                return "No authors found";
            }
            return authorList.stream().map(authorFormatter::format).collect(Collectors.joining());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Невозможно получить список книг:" + e.getMessage();
        }
    }

    @ShellMethod(value = "find author by id", key="author-by-id")
    public String authorById(long id) {
        Author author = authorService.findById(id);
        if (null != author) {
            return authorFormatter.format(author);
        }
        return "No authors found";
    }

}
