package com.example.books.cli;

import com.example.books.model.Author;
import com.example.books.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.CollectionUtils;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@ShellComponent
public class AuthorShellCommand {

    private final Logger logger = LoggerFactory.getLogger(AuthorShellCommand.class);

    private final AuthorService authorService;

    @ShellMethod(value = "list authors", key="list-authors")
    public String listAuthors() {
        try {
            List<Author> authorList = authorService.findAll();

            if (CollectionUtils.isEmpty(authorList)) {
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
            Author author = authorService.findById(id);

            if (Objects.isNull(author)) {
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
            authorService.deleteById(id);
            return MessageFormat.format("Author with id {0} was deleted", id);
        } catch (Exception e) {
            return "Cannot delete author with id=" + id;
        }
    }

}
