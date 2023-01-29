package com.example.books.service.impl;

import com.example.books.dao.AuthorRepository;
import com.example.books.dao.BookRepository;
import com.example.books.model.Book;
import com.example.books.model.Genre;
import com.example.books.service.BookPresentationService;
import com.example.books.util.BookFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class BookPresentationServiceImpl implements BookPresentationService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final com.example.books.dao.GenreRepository genreRepository;

    @Transactional
    @Override
    public String listAllBooks() {
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
    }

    @Transactional
    @Override
    public String bookByID(long id) {
        Optional<Book> book = bookRepository.findById(id);

        if (!book.isPresent()) {
            return MessageFormat.format("Book with id={0} not found", id);
        }

        return new BookFormatter().format(book.get());
    }

    @Transactional
    @Override
    public String addBook(String title, String isbn, int issued, long authorId, String genreId) {
        Book book = new Book();
        book.setTitle(title);
        book.setIssueYear(issued);
        book.setIsbn(isbn);
        book.setAuthor(authorRepository.findById(authorId).orElse(null));

        Set<Genre> genres = new HashSet<>();
        String[] genreIdArray = genreId.split(",");
        for (String s : genreIdArray) {
            Optional<Genre> genre = genreRepository.findById(Long.parseLong(s));
            genre.ifPresent(genres::add);
        }
        book.setGenres(genres);

        Optional<Book> created = bookRepository.create(book);
        if(created.isPresent()) {
            return new BookFormatter().format(created.get());
        }
        else {
            return "Cannot add a new book";
        }
    }
}
