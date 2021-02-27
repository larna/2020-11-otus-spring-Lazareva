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
import org.springframework.data.jpa.domain.Specification;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.BookSearchSpecification;

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
    private BookRepository repository;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(repository);
    }

    @DisplayName("Должен сохранять книгу")
    @ParameterizedTest
    @CsvSource(value = {", New book, ISBN-1234", "10, New book, ISBN-1234", "5, Updated book, ISBN-2345"})
    void shouldInvokeSaveForInsertOrUpdateBook(Long id, String name, String isbn) {
        Genre genre = Genre.builder().id(1L).name("Фантастика").build();
        Author author = Author.builder().id(1L).name("Марк Твен").birthday(LocalDate.of(1910, 4, 21)).build();
        Book newBook = Book.builder().id(id).name(name).isbn(isbn).genre(genre).authors(List.of(author)).build();
        bookService.save(newBook);
        Mockito.verify(repository, Mockito.times(1)).save(eq(newBook));
    }
    @DisplayName("Должен обновлять название книги")
    @Test
    void shouldUpdateBookName() {
        final Long bookId = 1L;
        final String expectedNewBookName = "Новое название";
        Genre genre = Genre.builder().id(1L).name("Фантастика").build();
        Author author = Author.builder().id(1L).name("Марк Твен").birthday(LocalDate.of(1910, 4, 21)).build();
        Book book = Book.builder().id(bookId).name("Уже существующая книга").isbn("ISBN - 1234").genre(genre).authors(List.of(author)).build();
        Book expectedBook = Book.builder().id(bookId).name(expectedNewBookName).isbn("ISBN - 1234").genre(genre).authors(List.of(author)).build();

        Mockito.when(repository.findById(bookId)).thenReturn(Optional.of(book));

        bookService.updateBookName(bookId, expectedNewBookName);
        Mockito.verify(repository, Mockito.times(1)).findById(eq(bookId));
        Mockito.verify(repository, Mockito.times(1)).save(eq(expectedBook));
        Mockito.verify(repository, Mockito.times(1)).findBookById(eq(bookId));
    }

    @DisplayName("При обновлении названия книги должен выбрасывать исключение если книги не существует")
    @Test
    void shouldThrowExceptionForUpdateBookNameIfBookNotExists() {
        final Long bookId = 10L;
        Mockito.when(repository.findById(bookId)).thenThrow(BookNotFoundException.class);

        assertThrows(BookNotFoundException.class, () -> bookService.updateBookName(bookId, "Новое название"));
        Mockito.verify(repository, Mockito.times(1)).findById(eq(bookId));
        Mockito.verify(repository, Mockito.times(0)).findBookById(eq(bookId));
        Mockito.verify(repository, Mockito.times(0)).save(any());
    }
    @DisplayName("При удалении должен вызывать метод delete repository")
    @Test
    void shouldInvokeDeleteOfRepository() {
        final Long bookId = 10L;
        Mockito.when(repository.existsById(bookId)).thenReturn(true);

        bookService.delete(bookId);
        Mockito.verify(repository, Mockito.times(1)).existsById(eq(bookId));
        Mockito.verify(repository, Mockito.times(1)).deleteById(eq(bookId));
    }
    @DisplayName("При удалении должен выбрасывать исключение если книги не существует")
    @Test
    void shouldThrowExceptionForDeleteIfBookNotExists() {
        final Long bookId = 10L;
        Mockito.when(repository.existsById(bookId)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.delete(bookId));
        Mockito.verify(repository, Mockito.times(1)).existsById(eq(bookId));
        Mockito.verify(repository, Mockito.times(0)).deleteById(eq(bookId));
    }

    @DisplayName("При поиске по id должен вызывать метод getById repository")
    @Test
    void shouldInvokeGetByIdOfRepository() {
        final Long bookId = 10L;
        Mockito.when(repository.existsById(bookId)).thenReturn(true);

        bookService.findById(bookId);
        Mockito.verify(repository, Mockito.times(1)).existsById(eq(bookId));
        Mockito.verify(repository, Mockito.times(1)).findBookById(eq(bookId));
    }
    @DisplayName("При поиске по id должен выбрасывать исключение если книги не существует")
    @Test
    void shouldThrowExceptionForFindByIdIfBookNotExists() {
        final Long bookId = 10L;
        Mockito.when(repository.existsById(bookId)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.findById(bookId));
        Mockito.verify(repository, Mockito.times(1)).existsById(eq(bookId));
        Mockito.verify(repository, Mockito.times(0)).findBookById(eq(bookId));
    }
    @DisplayName("При поиске по isbn должен вызывать метод getByIsbn repository")
    @Test
    void shouldInvokeGetByIsbnOfRepository() {
        final String isbn = "ISBN-1234";
        Mockito.when(repository.existsBookByIsbn(isbn)).thenReturn(true);

        bookService.findByIsbn(isbn);
        Mockito.verify(repository, Mockito.times(1)).existsBookByIsbn(eq(isbn));
        Mockito.verify(repository, Mockito.times(1)).findBookByIsbn(eq(isbn));
    }
    @DisplayName("При поиске по isbn должен выбрасывать исключение если книги не существует")
    @Test
    void shouldThrowExceptionForFindByIsbnIfBookNotExists() {
        final String isbn = "ISBN-1234";
        Mockito.when(repository.existsBookByIsbn(isbn)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.findByIsbn(isbn));
        Mockito.verify(repository, Mockito.times(1)).existsBookByIsbn(eq(isbn));
        Mockito.verify(repository, Mockito.times(0)).findBookByIsbn(eq(isbn));
    }
    @DisplayName("При поиске по автору должен вызываться метод getAllByAuthor repository")
    @Test
    void shouldInvokeGetByAuthorOfRepository() {
        bookService.findByAuthor(any());
        Mockito.verify(repository, Mockito.times(1)).findAllByAuthorsIs(any());
    }
    @DisplayName("При поиске по жанру должен вызываться метод getAllByGenre repository")
    @Test
    void shouldInvokeGetByGenreOfRepository() {
        bookService.findByGenre(any());
        Mockito.verify(repository, Mockito.times(1)).findAllByGenre(any());
    }
    @DisplayName("При поиске всех книг должен вызываться метод getAll repository")
    @Test
    void shouldInvokeGetAllOfRepository() {
        bookService.findAll(PageRequest.of(0,10));
        Mockito.verify(repository, Mockito.times(1)).findAll(eq(PageRequest.of(0,10)));
    }
    @DisplayName("При поиске всех книг по фильтру должен вызываться метод getAll со спецификацией, если фильтр не пуст")
    @Test
    void shouldInvokeGetAllWithFilter() {
        SearchFilter filter = new SearchFilter("Test", "Test", "Test");
        Specification<Book> spec = new BookSearchSpecification(filter);
        Pageable pageable = PageRequest.of(0,10);
        bookService.findAllByFilter(filter, pageable);
        Mockito.verify(repository, Mockito.times(1)).findAll((Specification) any(),(Pageable) any());
    }
}