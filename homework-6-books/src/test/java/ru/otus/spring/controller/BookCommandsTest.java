package ru.otus.spring.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.shell.Shell;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.controller.ui.View;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.services.books.BookNotFoundException;
import ru.otus.spring.services.books.BookService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Класс BookCommands")
@SpringBootTest
@ActiveProfiles("test")
class BookCommandsTest {
    @MockBean
    private BookService bookService;
    @MockBean(name="booksView")
    private View<Book> booksVew;
    @MockBean(name="bookWithCommentsView")
    private View<Book> bookWithCommentsVew;

    @Autowired
    private Shell shell;


    @DisplayName("Должен сохранять книгу")
    @ParameterizedTest
    @CsvSource(value = {"b+ -n Test -g 1 -a 1",
            "b+ -n Test -i ISBN-1234 -g 1 -a 1",
            "book-insert -n Test -g 1 -a 1",
            "book-insert -n Test -i ISBN-1234 -g 1 -a 1",
            "b+ -name Test -isbn ISBN-1234 -genre 1 -authors 1",
            "book-insert -name Test -genre 1 -authors 1",
    })
    void shouldCreateBook(String command) {
        final String expectedMessage = "Книга успешно добавлена";
        Genre genre = Genre.builder().id(1L).name("Test").build();
        Author author = Author.builder().id(1L).name("Test").birthday(LocalDate.now()).build();
        Book book = Book.builder().id(1L).name("Test").genre(genre).authors(List.of(author)).build();

        given(bookService.save(any())).willReturn(book);
        given(booksVew.getObjectView(any(), any())).willReturn(expectedMessage);

        Object actual = shell.evaluate(() -> command);
        Mockito.verify(bookService, Mockito.times(1)).save(any());
        assertThat(actual).isEqualTo(expectedMessage);
    }

    @DisplayName("Должен выводить сообщение об ошибке, если автора или жанра не существует")
    @ParameterizedTest
    @CsvSource(value = {"b+ -n Test -g 1 -a 1", "book-insert -n Test -g 1 -a 1",})
    void shouldShowMessageErrorIfAuthorOrGenreNotExists(String command) {
        final String expectedMessage = "Книгу не удалось добавить. Проверьте есть ли выбранный вами жанр или авторы...";
        given(bookService.save(any())).willThrow(BookNotFoundException.class);
        Object actual = shell.evaluate(() -> command);
        Mockito.verify(bookService, Mockito.times(1)).save(any());
        assertThat(actual).isEqualTo(expectedMessage);
    }

    @DisplayName("Должен выводить сообщение об ошибке, если id автора задан неверно")
    @ParameterizedTest
    @CsvSource(value = {"b+ -n Test -g 1 -a ff", "book-insert -n Test -g 1 -a 1o",})
    void shouldShowMessageErrorIfAuthorIsNotNumber(String command) {
        final String expectedMessage = "Идентификатор автора должен быть числовым!";
        Object actual = shell.evaluate(() -> command);
        Mockito.verify(bookService, Mockito.times(0)).save(any());
        assertThat(actual).isEqualTo(expectedMessage);
    }

    @DisplayName("Должен обновлять книгу")
    @ParameterizedTest
    @CsvSource(value = {"book-update 10 -n Test -g 1 -a 1",
            "bu 10 -n Test -g 1 -a 1",
            "book-update -id 1 -n Test -i ISBN-1234 -g 1 -a 1",
            "book-update -id 1 -name Test -isbn ISBN-1234 -genre 1 -authors 1",
            "book-update -id 1 -name Test -genre 1 -authors 1",
    })
    void shouldUpdateBook(String command) {
        final String expectedMessage = "Книга успешно изменена";
        Genre genre = Genre.builder().id(1L).name("Test").build();
        Author author = Author.builder().id(1L).name("Test").birthday(LocalDate.now()).build();
        Book book = Book.builder().id(1L).name("Test").genre(genre).authors(List.of(author)).build();

        Object actual = shell.evaluate(() -> command);
        Mockito.verify(bookService, Mockito.times(1)).save(any());
    }

    @DisplayName("При обновлении должен выводить сообщение об ошибке, если id автора задан неверно")
    @ParameterizedTest
    @CsvSource(value = {"book-update -id 1 -n Test -g 1 -a 1o", "bu -id 1 -n Test -g 1 -a 1o"})
    void shouldUpdateShowMessageErrorIfAuthorIsNotNumber(String command) {
        final String expectedMessage = "Идентификатор автора должен быть числовым!";
        Object actual = shell.evaluate(() -> command);
        Mockito.verify(bookService, Mockito.times(0)).save(any());
        assertThat(actual).isEqualTo(expectedMessage);
    }

    @DisplayName("Должен изменять название книги")
    @ParameterizedTest
    @CsvSource(value = {"book-update-name 10 -n Test", "bun 10 -n Test"})
    void shouldUpdateBookName(String command) {
        final String expectedMessage = "Книга успешно изменена";
        Genre genre = Genre.builder().id(1L).name("Test").build();
        Author author = Author.builder().id(1L).name("Test").birthday(LocalDate.now()).build();
        Book book = Book.builder().id(1L).name("Test").genre(genre).authors(List.of(author)).build();

        given(bookService.updateBookName(any(), any())).willReturn(book);
        given(booksVew.getObjectView(any(), any())).willReturn(expectedMessage);

        Object actual = shell.evaluate(() -> command);
        Mockito.verify(bookService, Mockito.times(1)).updateBookName(any(), any());
        assertThat(actual).isEqualTo(expectedMessage);
    }

    @DisplayName("При изменении названия книги должен выводить сообщение об ошибке, если книги с таким id не существует")
    @ParameterizedTest
    @CsvSource(value = {"book-update-name -id 1 -n Test", "bun -id 1 -n Test"})
    void shouldUpdateBookNameShowMessageErrorIfAuthorOrGenreNotExists(String command) {
        final String expectedMessage = "Книгу с заданным id не удалось найти...";
        given(bookService.updateBookName(any(), any())).willThrow(BookNotFoundException.class);
        Object actual = shell.evaluate(() -> command);
        Mockito.verify(bookService, Mockito.times(1)).updateBookName(any(), any());
        assertThat(actual).isEqualTo(expectedMessage);
    }

    @DisplayName("Должен удалять книгу")
    @ParameterizedTest
    @CsvSource(value = {"book-delete -id 1", "b- -id 1", "b- 1"})
    void shouldDeleteBook(String command) {
        final String expectedMessage = "Книга успешно удалена";

        Object actual = shell.evaluate(() -> command);
        Mockito.verify(bookService, Mockito.times(1)).delete(any());
        assertThat(actual).isEqualTo(expectedMessage);
    }

    @DisplayName("Должен показывать сообщение об ошибке если книга с таким id не найдена")
    @ParameterizedTest
    @CsvSource(value = {"book-delete -id 1", "b- -id 1", "b- 1"})
    void shouldDeleteShowErrorMessageIfBookNotExists(String command) {
        final String expectedMessage = "Книгу не удалось удалить. Книга с таким id не найдена...";
        BDDMockito.doThrow(BookNotFoundException.class).when(bookService).delete(any());
        Object actual = shell.evaluate(() -> command);
        Mockito.verify(bookService, Mockito.times(1)).delete(any());
        assertThat(actual).isEqualTo(expectedMessage);
    }

    @DisplayName("Показывает книги")
    @Test
    void shouldFindAllBooks() {
        final String expectedMessage = "Все книги";
        Genre genre = Genre.builder().id(1L).name("Test").build();
        Page<Book> page = new PageImpl<>(List.of(Book.builder().id(1L).name("Test").genre(genre).authors(List.of()).build()));

        given(booksVew.getListView(any(), any())).willReturn(expectedMessage);
        given(bookService.findAll(any())).willReturn(page);

        Object actual = shell.evaluate(() -> "b");
        Mockito.verify(bookService, Mockito.times(1)).findAll(any());
        assertThat(actual).isEqualTo(expectedMessage);
    }

    @DisplayName("Показывает сообщение о том, что книги не найдены")
    @Test
    void shouldFindAllShowMessageIfBooksNotFound() {
        final String expectedMessage = "Книги не найдены";
        given(bookService.findAll(any())).willReturn(Page.empty());

        Object actual = shell.evaluate(() -> "b");
        Mockito.verify(bookService, Mockito.times(1)).findAll(any());
        assertThat(actual).isEqualTo(expectedMessage);
    }

    @DisplayName("Показывает сообщение об ошибке если неправильно заданы парамерты страницы")
    @ParameterizedTest
    @CsvSource(value = {"b -1 10", "b 0 -10", "books -1 10", "books 0 -10"})
    void shouldFindAllMessageIfPageOrSizeUncorrected(String command) {
        final String expectedMessage = "Страница или размер не могут быть отрицательными!";

        Object actual = shell.evaluate(() -> command);
        Mockito.verify(bookService, Mockito.times(0)).findAll(any());
        assertThat(actual).isEqualTo(expectedMessage);
    }

    @DisplayName("Поиск по фильтру показывает сообщение об ошибке если неправильно заданы парамерты страницы")
    @ParameterizedTest
    @CsvSource(value = {"b? -p -1 -s 10", "b? -p 0 -s -10", "books-find -p -1 -s 10", "books-find -p 0 -s -10"})
    void shouldFindBooksShowMessageIfPageOrSizeUncorrected(String command) {
        final String expectedMessage = "Страница или размер не могут быть отрицательными!";

        Object actual = shell.evaluate(() -> command);
        Mockito.verify(bookService, Mockito.times(0)).findAllByFilter(any(), any());
        assertThat(actual).isEqualTo(expectedMessage);
    }

    @DisplayName("Поиск по фильтру показывает сообщение если книги не найдены")
    @Test
    void shouldFindBooksShowMessageIfBooksNotFound() {
        final String expectedMessage = "Книги не найдены";
        given(bookService.findAllByFilter(any(), any())).willReturn(Page.empty());

        Object actual = shell.evaluate(() -> "b? -p 0 -s 10");
        Mockito.verify(bookService, Mockito.times(1)).findAllByFilter(any(), any());
        assertThat(actual).isEqualTo(expectedMessage);
    }

    @DisplayName("Поиск по фильтру показывает найденные книги")
    @Test
    void shouldCorrectFindBooks() {
        final String expectedMessage = "Книги найдены";
        Genre genre = Genre.builder().id(1L).name("Test").build();
        Page<Book> page = new PageImpl<>(List.of(Book.builder().id(1L).name("Test").genre(genre).authors(List.of()).build()));

        given(booksVew.getListView(any(), any())).willReturn(expectedMessage);
        given(bookService.findAllByFilter(any(), any())).willReturn(page);

        Object actual = shell.evaluate(() -> "b? -p 0 -s 10");
        Mockito.verify(bookService, Mockito.times(1)).findAllByFilter(any(), any());
        assertThat(actual).isEqualTo(expectedMessage);
    }

    @DisplayName("Поиск по id показывает найденные книги")
    @ParameterizedTest
    @CsvSource(value = {"b?id 1", "book-by-id 1", "b?id -id 1", "book-by-id -id 1"})
    void shouldCorrectFindBookById(String command) {
        final String expectedMessage = "Книга успешно найдена";
        Genre genre = Genre.builder().id(1L).name("Test").build();
        Book book = Book.builder().id(1L).name("Test").genre(genre).authors(List.of()).build();

        given(bookWithCommentsVew.getObjectView(any(), any())).willReturn(expectedMessage);
        given(bookService.findById(any())).willReturn(book);

        Object actual = shell.evaluate(() -> command);
        Mockito.verify(bookService, Mockito.times(1)).findById(any());
        assertThat(actual).isEqualTo(expectedMessage);
    }

    @DisplayName("Поиск по id показывает сообщение об ошибке, если книга не найдена")
    @ParameterizedTest
    @CsvSource(value = {"b?id 1", "book-by-id 1"})
    void shouldFindBookByIdShowErrorMessage(String command) {
        final String expectedMessage = "Книга c id=1 не найдена";
        given(bookService.findById(any())).willThrow(BookNotFoundException.class);

        Object actual = shell.evaluate(() -> command);
        Mockito.verify(bookService, Mockito.times(1)).findById(any());
        assertThat(actual).isEqualTo(expectedMessage);
    }
}