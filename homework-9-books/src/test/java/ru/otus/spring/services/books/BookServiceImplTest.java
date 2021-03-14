package ru.otus.spring.services.books;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DisplayName("Класс BookServiceImpl")
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(bookRepository);
    }

    @DisplayName("Должен сохранять книгу")
    @ParameterizedTest
    @CsvSource(value = {", New book, ISBN-1234", "10, New book, ISBN-1234", "5, Updated book, ISBN-2345"})
    void shouldInvokeSaveForInsertOrUpdateBook(Long id, String name, String isbn) {
        Genre genre = Genre.builder().id(1L).name("Фантастика").build();
        Author author = Author.builder().id(1L).name("Марк Твен").birthday(LocalDate.of(1910, 4, 21)).build();
        Book newBook = Book.builder().id(id).name(name).isbn(isbn).genre(genre).authors(List.of(author)).build();
        bookService.save(newBook);
        Mockito.verify(bookRepository, Mockito.times(1)).save(eq(newBook));
    }

    @DisplayName("Должен обновлять название книги")
    @Test
    void shouldUpdateBookName() {
        final Long bookId = 1L;
        final String expectedNewBookName = "Новое название";
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));

        bookService.updateBookName(bookId, expectedNewBookName);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(eq(bookId));
        Mockito.verify(bookRepository, Mockito.times(1)).save(any());
        Mockito.verify(bookRepository, Mockito.times(1)).findById(eq(bookId));
    }

    @DisplayName("При обновлении названия книги должен выбрасывать исключение если книги не существует")
    @Test
    void shouldThrowExceptionForUpdateBookNameIfBookNotExists() {
        final Long bookId = 10L;
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.ofNullable(null));

        assertThrows(BookNotFoundException.class, () -> bookService.updateBookName(bookId, "Новое название"));
        Mockito.verify(bookRepository, Mockito.times(1)).findById(eq(bookId));
        Mockito.verify(bookRepository, Mockito.times(0)).save(any());
    }

    @DisplayName("При удалении должен вызывать метод delete repository")
    @Test
    void shouldInvokeDeleteOfRepository() {
        final Long bookId = 10L;
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));

        bookService.delete(bookId);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(eq(bookId));
        Mockito.verify(bookRepository, Mockito.times(1)).delete(any());
    }

    @DisplayName("При удалении должен выбрасывать исключение если книги не существует")
    @Test
    void shouldThrowExceptionForDeleteIfBookNotExists() {
        final Long bookId = 10L;
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.ofNullable(null));

        assertThrows(BookNotFoundException.class, () -> bookService.delete(bookId));
        Mockito.verify(bookRepository, Mockito.times(1)).findById(eq(bookId));
        Mockito.verify(bookRepository, Mockito.times(0)).delete(any());
    }

    @DisplayName("При поиске по id должен вызывать метод getById repository")
    @Test
    void shouldInvokeGetByIdOfRepository() {
        final Long bookId = 7L;
        Book defaultBook = Book.builder().id(bookId).comments(List.of()).build();
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(defaultBook));

        bookService.findById(bookId);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(any());
    }

    @DisplayName("При поиске по id должен выбрасывать исключение если книги не существует")
    @Test
    void shouldThrowExceptionForFindByIdIfBookNotExists() {
        final Long bookId = 10L;
        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(BookNotFoundException.class, () -> bookService.findById(bookId));
        Mockito.verify(bookRepository, Mockito.times(1)).findById(eq(bookId));
    }

    @DisplayName("При поиске всех книг должен вызываться метод getAll repository")
    @Test
    void shouldInvokeGetAllOfRepository() {
        bookService.findAll(PageRequest.of(0, 10));
        Mockito.verify(bookRepository, Mockito.times(1)).findAll(eq(PageRequest.of(0, 10)));
    }

    @DisplayName("При поиске всех книг по фильтру должен вызываться метод getAll со спецификацией, если фильтр не пуст")
    @Test
    void shouldInvokeGetAllWithFilter() {
        SearchFilter filter = new SearchFilter("Test", "Test", "Test");
        BookSearchSpecification spec = new BookSearchSpecification(filter);
        Pageable pageable = PageRequest.of(0, 10);
        bookService.findAllByFilter(filter, pageable);
        Mockito.verify(bookRepository, Mockito.times(1)).findAll(any(), (Pageable) any());
    }
}