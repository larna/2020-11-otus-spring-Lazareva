package ru.otus.spring.services.books;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.books.BookRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс BookServiceImpl")
class BookServiceImplTest {
    private final BookRepository repository;
    private final BookService bookService;

    public BookServiceImplTest(@Mock BookRepository repository) {
        this.repository = repository;
        this.bookService = new BookServiceImpl(repository);
    }

    @DisplayName("Должен вызывать метод insert репозитория для новой книги")
    @ParameterizedTest
    @CsvSource(value = {", New book, ISBN-1234", "10, New book, ISBN-1234"})
    void shouldInsertNewBook(Long id, String name, String isbn) {
        Book newBook = new Book(id, name, isbn, new Genre(1L, "Фантастика"),
                List.of(new Author(1L, "Марк Твен", null, LocalDate.of(1910, 4, 21))));
        bookService.save(newBook);
        Mockito.verify(repository, Mockito.times(1)).insert(eq(newBook));
    }

    @DisplayName("Должен вызывать метод update репозитория для уже существующей книги")
    @Test
    void shouldUpdateForExistsBook() {
        Mockito.when(repository.isExistsById(1L)).thenReturn(true);
        Book newBook = new Book(1L, "Уже существующая книга", "ISBN - 1234",
                new Genre(1L, "Фантастика"),
                List.of(new Author(1L, "Марк Твен", null, LocalDate.of(1910, 4, 21))));
        bookService.save(newBook);
        Mockito.verify(repository, Mockito.times(1)).update(eq(newBook));
    }

    @DisplayName("Должен обновлять название книги")
    @Test
    void shouldUpdateBookName() {
        final Long bookId = 1L;
        final String expectedNewBookName = "Новое название";
        Book book = new Book(1L, "Уже существующая книга", "ISBN - 1234",
                new Genre(1L, "Фантастика"),
                List.of(new Author(1L, "Марк Твен", null, LocalDate.of(1910, 4, 21))));

        Book expectedBook = new Book(1L, expectedNewBookName, "ISBN - 1234",
                new Genre(1L, "Фантастика"),
                List.of(new Author(1L, "Марк Твен", null, LocalDate.of(1910, 4, 21))));

        Mockito.when(repository.isExistsById(bookId)).thenReturn(true);
        Mockito.when(repository.getById(bookId)).thenReturn(book);

        bookService.updateBookName(bookId, expectedNewBookName);
        Mockito.verify(repository, Mockito.times(1)).getById(eq(bookId));
        Mockito.verify(repository, Mockito.times(1)).update(eq(expectedBook));
    }

    @DisplayName("ДПри обновлении названия книги длжен выбрасывать исключение если книги не существует")
    @Test
    void shouldThrowExceptionForUpdateBookNameIfBookNotExists() {
        final Long bookId = 10L;
        Mockito.when(repository.isExistsById(bookId)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.updateBookName(bookId, "Новое название"));
        Mockito.verify(repository, Mockito.times(1)).isExistsById(eq(bookId));
        Mockito.verify(repository, Mockito.times(0)).getById(eq(bookId));
        Mockito.verify(repository, Mockito.times(0)).update(any());
    }
    @DisplayName("При удалении должен вызывать метод delete repository")
    @Test
    void shouldInvokeDeleteOfRepository() {
        final Long bookId = 10L;
        Mockito.when(repository.isExistsById(bookId)).thenReturn(true);

        bookService.delete(bookId);
        Mockito.verify(repository, Mockito.times(1)).isExistsById(eq(bookId));
        Mockito.verify(repository, Mockito.times(1)).delete(eq(bookId));
    }
    @DisplayName("При удалении должен выбрасывать исключение если книги не существует")
    @Test
    void shouldThrowExceptionForDeleteIfBookNotExists() {
        final Long bookId = 10L;
        Mockito.when(repository.isExistsById(bookId)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.delete(bookId));
        Mockito.verify(repository, Mockito.times(1)).isExistsById(eq(bookId));
        Mockito.verify(repository, Mockito.times(0)).delete(eq(bookId));
    }

    @DisplayName("При поиске по id должен вызывать метод getById repository")
    @Test
    void shouldInvokeGetByIdOfRepository() {
        final Long bookId = 10L;
        Mockito.when(repository.isExistsById(bookId)).thenReturn(true);

        bookService.findById(bookId);
        Mockito.verify(repository, Mockito.times(1)).isExistsById(eq(bookId));
        Mockito.verify(repository, Mockito.times(1)).getById(eq(bookId));
    }
    @DisplayName("При поиске по id должен выбрасывать исключение если книги не существует")
    @Test
    void shouldThrowExceptionForFindByIdIfBookNotExists() {
        final Long bookId = 10L;
        Mockito.when(repository.isExistsById(bookId)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.findById(bookId));
        Mockito.verify(repository, Mockito.times(1)).isExistsById(eq(bookId));
        Mockito.verify(repository, Mockito.times(0)).getById(eq(bookId));
    }
    @DisplayName("При поиске по isbn должен вызывать метод getByIsbn repository")
    @Test
    void shouldInvokeGetByIsbnOfRepository() {
        final String isbn = "ISBN-1234";
        Mockito.when(repository.isExistsByIsbn(isbn)).thenReturn(true);

        bookService.findByIsbn(isbn);
        Mockito.verify(repository, Mockito.times(1)).isExistsByIsbn(eq(isbn));
        Mockito.verify(repository, Mockito.times(1)).getByIsbn(eq(isbn));
    }
    @DisplayName("При поиске по isbn должен выбрасывать исключение если книги не существует")
    @Test
    void shouldThrowExceptionForFindByIsbnIfBookNotExists() {
        final String isbn = "ISBN-1234";
        Mockito.when(repository.isExistsByIsbn(isbn)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.findByIsbn(isbn));
        Mockito.verify(repository, Mockito.times(1)).isExistsByIsbn(eq(isbn));
        Mockito.verify(repository, Mockito.times(0)).getByIsbn(eq(isbn));
    }
    @DisplayName("При поиске по автору должен вызываться метод getAllByAuthor repository")
    @Test
    void shouldInvokeGetByAuthorOfRepository() {
        bookService.findByAuthor(any());
        Mockito.verify(repository, Mockito.times(1)).getAllByAuthor(any());
    }
    @DisplayName("При поиске по жанру должен вызываться метод getAllByGenre repository")
    @Test
    void shouldInvokeGetByGenreOfRepository() {
        bookService.findByGenre(any());
        Mockito.verify(repository, Mockito.times(1)).getAllByGenre(any());
    }
    @DisplayName("При поиске всех книг должен вызываться метод getAll repository")
    @Test
    void shouldInvokeGetAllOfRepository() {
        bookService.findAll(PageRequest.of(0,10));
        Mockito.verify(repository, Mockito.times(1)).getAll(any());
    }
    @DisplayName("При поиске всех книг по фильтру должен вызываться метод getAll со спецификацией, если фильтр не пуст")
    @Test
    void shouldInvokeGetAllWithFilter() {
        SearchFilter filter = new SearchFilter("Test", "Test", "Test");
        bookService.findAllByFilter(filter, PageRequest.of(0,10));
        Mockito.verify(repository, Mockito.times(1)).getAll(any(),any());
    }
    @DisplayName("При поиске всех книг по фильтру должен вызываться метод getAll без спецификации, если фильтр пуст")
    @Test
    void shouldInvokeGetAllWithoutFilter() {
        SearchFilter filter = new SearchFilter(null, null, null);
        bookService.findAllByFilter(filter,any());
        Mockito.verify(repository, Mockito.times(1)).getAll(any());
    }
}