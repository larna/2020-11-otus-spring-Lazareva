package ru.otus.spring.controller.events;

import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.otus.spring.controller.ui.View;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.services.authors.AuthorService;
import ru.otus.spring.services.books.BookService;
import ru.otus.spring.services.genres.GenreService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Обработчик события тестирования студента
 */
@Component
public class BookUpdateHandler {
    /**
     * Сервис для работы с книгами
     */
    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;
    /**
     * Отображение книг пользователю
     */
    private final View<Book> booksView;


    public BookUpdateHandler(BookService bookService, GenreService genreService, AuthorService authorService, View<Book> booksView) {
        this.bookService = bookService;
        this.genreService = genreService;
        this.authorService = authorService;
        this.booksView = booksView;
    }

    @SneakyThrows
    @EventListener
    public void onApplicationEvent(BookEvent event) {
        Thread.sleep(100);
        try {
            Book book = bookService.findById(event.getBook().getId());
            book.setName(event.getBook().getName());
            book.setIsbn(event.getBook().getIsbn());
            refreshAuthors(book);
            refreshGenre(book);
            bookService.save(book);
            System.out.println(booksView.getObjectView(book, "Книга успешно изменена"));
        } catch (Exception e) {
            System.out.println("Книгу не удалось изменить. Проверьте есть ли выбранный вами жанр или авторы...");
        }
    }

    private void refreshAuthors(Book book) {
        if (book.getAuthors() == null)
            return;
        List<Long> authorsId = book.getAuthors().stream().map(author -> author.getId()).collect(Collectors.toList());
        List<Author> authors = authorService.findAllByIdIn(authorsId);
        book.getAuthors().clear();
        book.getAuthors().addAll(authors);
    }

    private void refreshGenre(Book book) {
        if (book.getGenre() == null)
            return;
        Genre genre = genreService.findById(book.getGenre().getId());
        book.setGenre(genre);
    }
}
