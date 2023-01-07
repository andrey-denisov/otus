package com.example.books.cli;

import com.example.books.model.Author;
import com.example.books.util.AuthorFormatter;
import com.example.books.service.AuthorService;
import com.example.books.util.ListFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@ShellComponent
public class AuthorShellCommand {
    private final Logger logger = LoggerFactory.getLogger(AuthorShellCommand.class);
    private final AuthorService authorService;
    private final AuthorFormatter authorFormatter;

    @ShellMethod(value = "list authors", key="list-authors")
    public String listAuthors() {
        try {
            List<Author> authorList = authorService.findAll();
            if (CollectionUtils.isEmpty(authorList)) {
                return "No authors found";
            }
            return ListFormatter.format(authorList, authorFormatter, "\n");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "Cannot get list of authors:" + e.getMessage();
        }
    }

    @ShellMethod(value = "find author by id", key="author-by-id")
    public String authorById(long id) {
        Author author = authorService.findById(id);
        if (Objects.nonNull(author)) {
            return authorFormatter.format(author);
        }
        return "No authors found";
    }

}
