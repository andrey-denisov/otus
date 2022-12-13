package com.example.books.cli;

import com.example.books.dao.AuthorRepository;
import com.example.books.model.Author;
import org.springframework.shell.standard.ShellComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;


@ShellComponent
public class AuthorShellCommand {

    private final Logger logger = LoggerFactory.getLogger(AuthorShellCommand.class);

    private final AuthorRepository authorRepository;

    public AuthorShellCommand(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @ShellMethod(value = "list authors", key="list-authors")
    public String listAuthors() {
        try {
            List<Author> authorList = authorRepository.findAll();

            if (null == authorList || authorList.isEmpty()) {
                return "No authors found";
            }
            return authorList.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Невозможно получить список книг:" + e.getMessage();
        }
    }

    @ShellMethod(value = "find author by id", key="author-by-id")
    public String authorById(long id) {
        try {
            Author author = authorRepository.findById(id);

            if (null == author) {
                return "No authors found";
            }
            return author.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Невозможно найти автора по id: " + e.getMessage();
        }
    }

}
