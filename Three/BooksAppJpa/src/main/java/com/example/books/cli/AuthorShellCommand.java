package com.example.books.cli;

import com.example.books.dao.impl.AuthorRepositoryImpl;
import com.example.books.model.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class AuthorShellCommand {

    private final Logger logger = LoggerFactory.getLogger(AuthorShellCommand.class);

    private final AuthorRepositoryImpl authorRepository;

    public AuthorShellCommand(AuthorRepositoryImpl authorRepository) {
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
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Cannot obtain a list of authors: " + e.getMessage();
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
            return "Cannot find author with id=" + id + ": " + e.getMessage();
        }
    }

    @ShellMethod(value = "delete author by id", key="delete-author-by-id")
    public String deleteById(long id) {
        try {
            Author author = authorRepository.deleteById(id);
            return MessageFormat.format("Author {0} was deleted", author);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Cannot delete author with id=" + id + ": " + e.getMessage();
        }
    }

}
