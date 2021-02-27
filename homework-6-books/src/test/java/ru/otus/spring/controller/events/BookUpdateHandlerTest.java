package ru.otus.spring.controller.events;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.controller.ui.View;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.services.authors.AuthorNotFoundException;
import ru.otus.spring.services.authors.AuthorService;
import ru.otus.spring.services.books.BookNotFoundException;
import ru.otus.spring.services.books.BookService;
import ru.otus.spring.services.genres.GenreNotFoundException;
import ru.otus.spring.services.genres.GenreService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс BookUpdateHandler")
class BookUpdateHandlerTest {
    @Mock
    private BookService bookService;
    @Mock
    private GenreService genreService;
    @Mock
    private AuthorService authorService;
    @Mock
    private View<Book> booksView;
    private BookUpdateHandler handler;

    @BeforeEach
    void init() {
        handler = new BookUpdateHandler(bookService, genreService, authorService, booksView);
    }

    @DisplayName("При обновлении должен выбрасывать исключение если хотя бы одни из выбранных авторов не найден")
    @Test
    void shouldThrowAuthorNotFoundExceptionIfAnyOfAuthorsNotExists() {
        Genre genre = Genre.builder().id(1L).name("Test").build();
        Author author1 = Author.builder().id(1L).build();
        Author author2 = Author.builder().id(2L).build();
        Book book = Book.builder().id(1L).name("Test").genre(genre).authors(List.of(author1, author2)).build();
        BookEvent event = new BookEvent(this, book);
        given(authorService.findAllByIdIn(any())).willThrow(AuthorNotFoundException.class);

        handler.onApplicationEvent(event);

        Mockito.verify(authorService, Mockito.times(1)).findAllByIdIn(any());
        Mockito.verify(bookService, Mockito.times(0)).save(any());
        Mockito.verify(genreService, Mockito.times(0)).findById(any());
    }

    @DisplayName("При обновлении должен выбрасывать исключение если жанр не найден")
    @Test
    void shouldThrowGenreNotFoundExceptionIfGenreNotExists() {
        Genre genre = Genre.builder().id(1L).name("Test").build();
        Author author1 = Author.builder().id(10L).build();
        Author author2 = Author.builder().id(11L).build();
        Book book = Book.builder().id(1L).name("Test").genre(genre).authors(List.of(author1, author2)).build();
        BookEvent event = new BookEvent(this, book);
        given(authorService.findAllByIdIn(any())).willReturn(List.of(author1,author2));
        given(genreService.findById(any())).willThrow(GenreNotFoundException.class);

        handler.onApplicationEvent(event);

        Mockito.verify(authorService, Mockito.times(1)).findAllByIdIn(any());
        Mockito.verify(genreService, Mockito.times(1)).findById(any());
        Mockito.verify(bookService, Mockito.times(0)).save(any());
    }
    @DisplayName("Сохраняет книгу")
    @Test
    void shouldSaveBook() {
        Genre genre = Genre.builder().id(1L).name("Test").build();
        Author author1 = Author.builder().id(10L).build();
        Author author2 = Author.builder().id(11L).build();
        Book book = Book.builder().id(1L).name("Test").genre(genre).authors(List.of(author1, author2)).build();
        BookEvent event = new BookEvent(this, book);
        given(authorService.findAllByIdIn(any())).willReturn(List.of(author1,author2));
        given(genreService.findById(any())).willReturn(genre);

        handler.onApplicationEvent(event);

        Mockito.verify(authorService, Mockito.times(1)).findAllByIdIn(any());
        Mockito.verify(genreService, Mockito.times(1)).findById(any());
        Mockito.verify(bookService, Mockito.times(1)).save(any());
    }

}