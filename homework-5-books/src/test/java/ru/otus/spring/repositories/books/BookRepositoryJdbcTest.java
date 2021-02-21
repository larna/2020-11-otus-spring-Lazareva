package ru.otus.spring.repositories.books;

import lombok.val;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.authors.AuthorRepositoryJdbc;
import ru.otus.spring.services.books.BookNotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DisplayName("Репозиторий на основе Jdbc для работы с книгами")
@JdbcTest
@Import({BookRepositoryJdbc.class, AuthorRepositoryJdbc.class})
class BookRepositoryJdbcTest {
    private static final Integer EXPECTED_BOOK_COUNT = 5;
    private static final String EXPECTED_BOOK_NAME = "Трудно быть богом";
    private static final String EXPECTED_GENRE_NAME = "Фантастика";

    private static final String EXPECTED_AUTHOR_NAME_1 = "Стругацкий Аркадий Натанович";
    private static final LocalDate EXPECTED_BIRTHDAY_AUTHOR_1 = LocalDate.of(1925, 8, 28);

    private static final String EXPECTED_AUTHOR_NAME_2 = "Стругацкий Борис Натанович";
    private static final LocalDate EXPECTED_BIRTHDAY_AUTHOR_2 = LocalDate.of(1933, 4, 15);

    final static List<Author> EXPECTED_AUTHORS = List.of(new Author(4L, EXPECTED_AUTHOR_NAME_1, null, EXPECTED_BIRTHDAY_AUTHOR_1),
            new Author(5L, EXPECTED_AUTHOR_NAME_2, null, EXPECTED_BIRTHDAY_AUTHOR_2));

    private Condition<Book> containsAuthors = new Condition<>((book) ->
            book.getAuthors() != null && book.getAuthors().containsAll(EXPECTED_AUTHORS),
            "Содержит авторов " + EXPECTED_AUTHOR_NAME_1 + " " + EXPECTED_AUTHOR_NAME_2);

    @Autowired
    BookRepositoryJdbc bookRepositoryJdbc;

    private Book getBook() {
        return new Book(1L, EXPECTED_BOOK_NAME, null, new Genre(1L, EXPECTED_GENRE_NAME),
                List.of(new Author(4L, EXPECTED_AUTHOR_NAME_1, null, EXPECTED_BIRTHDAY_AUTHOR_1),
                        new Author(5L, EXPECTED_AUTHOR_NAME_2, null, EXPECTED_BIRTHDAY_AUTHOR_2)));
    }

    @DisplayName("должен загружать по-страничный список всех  книг с полной информацией о них")
    @Test
    void shouldReturnCorrectBooksPageWithAllInfo() {
        Book expectedBook = getBook();
        val actualBooks = bookRepositoryJdbc.getAll(PageRequest.of(0, 10));
        assertThat(actualBooks).isNotNull();
        assertThat(actualBooks.getContent()).hasSize(EXPECTED_BOOK_COUNT)
                .allMatch(book -> book.getAuthors() != null && book.getAuthors().size() > 0)
                .allMatch(book -> book.getGenre() != null)
                .allMatch(book -> book.getName() != null && !book.getName().isEmpty())
                .contains(expectedBook);
    }

    @DisplayName("должен выбрасывать исключение в случае если добавляется книга с уже существующим ключом")
    @Test
    void shouldThrowDuplicateKeyExceptionIfInsertBookWithDuplicateId() {
        Book newBook = getBook();
        assertThrows(DuplicateKeyException.class, () -> bookRepositoryJdbc.insert(newBook));
    }

    @DisplayName("должен корректно сохранять книгу")
    @Test
    void shouldCorrectInsertBook() {
        final Genre expectedGenre = new Genre(1L, EXPECTED_GENRE_NAME);
        final String expectedBookName = "Пикник на обочине";
        Book newBook = new Book(null, expectedBookName, null, new Genre(1L, EXPECTED_GENRE_NAME),
                List.of(new Author(4L, EXPECTED_AUTHOR_NAME_1, null, EXPECTED_BIRTHDAY_AUTHOR_1),
                        new Author(5L, EXPECTED_AUTHOR_NAME_2, null, EXPECTED_BIRTHDAY_AUTHOR_2)));

        Book actualBook = bookRepositoryJdbc.insert(newBook);
        assertThat(actualBook).isNotNull()
                .hasFieldOrPropertyWithValue("name", expectedBookName)
                .hasNoNullFieldsOrPropertiesExcept("isbn")
                .matches(book -> book.getId() != null)
                .matches(book -> expectedGenre.equals(book.getGenre()))
                .has(containsAuthors);
    }

    @DisplayName("должен корректно обновлять книгу")
    @Test
    void shouldCorrectUpdateBook() {
        final Long expectedId = 1L;
        final String expectedBookName = "Пикник на обочине";
        final String expectedISBN = "1234";
        final Genre expectedGenre = new Genre(1L, EXPECTED_GENRE_NAME);
        Book updatingBook = new Book(expectedId, expectedBookName, expectedISBN, new Genre(1L, EXPECTED_GENRE_NAME),
                List.of(new Author(4L, EXPECTED_AUTHOR_NAME_1, null, EXPECTED_BIRTHDAY_AUTHOR_1),
                        new Author(5L, EXPECTED_AUTHOR_NAME_2, null, EXPECTED_BIRTHDAY_AUTHOR_2)));
        bookRepositoryJdbc.update(updatingBook);
        Book actualBook = bookRepositoryJdbc.getById(updatingBook.getId());
        assertThat(actualBook).isNotNull()
                .hasFieldOrPropertyWithValue("name", expectedBookName)
                .hasFieldOrPropertyWithValue("isbn", expectedISBN)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .matches(book -> expectedGenre.equals(book.getGenre()))
                .has(containsAuthors);
    }

    @DisplayName("должен корректно удалять книгу")
    @Test
    void shouldCorrectDeleteBook() {
        final long bookId = 5L;
        bookRepositoryJdbc.delete(bookId);
        assertThrows(BookNotFoundException.class, () -> bookRepositoryJdbc.getById(bookId));
    }

    @DisplayName("должен корректно получать книгу по идентификатору")
    @Test
    void shouldCorrectGetById() {
        final long expectedId = 1L;
        final Genre expectedGenre = new Genre(1L, EXPECTED_GENRE_NAME);
        Book actualBook = bookRepositoryJdbc.getById(expectedId);
        assertThat(actualBook).isNotNull()
                .hasFieldOrPropertyWithValue("name", EXPECTED_BOOK_NAME)
                .hasNoNullFieldsOrPropertiesExcept("isbn")
                .hasFieldOrPropertyWithValue("id", expectedId)
                .matches(book -> expectedGenre.equals(book.getGenre()))
                .has(containsAuthors);
    }

    @DisplayName("должен корректно находить книги по жанру")
    @Test
    void shouldCorrectGetAllByGenre() {
        final int expectedListSize = 3;
        final Genre expectedGenre = new Genre(1L, EXPECTED_GENRE_NAME);
        final Book expectedBook = getBook();
        List<Book> actualBooks = bookRepositoryJdbc.getAllByGenre(expectedGenre);
        assertThat(actualBooks).isNotNull()
                .hasSize(expectedListSize)
                .contains(expectedBook);
    }

    @DisplayName("должен корректно находить книги по названию")
    @Test
    void shouldCorrectGetAllByBookName() {
        final int expectedBooksSize = 1;
        final Book expectedBook = getBook();
        Page<Book> actualBooks = bookRepositoryJdbc.getAllByLikeBookName(EXPECTED_BOOK_NAME, PageRequest.of(0, 10));
        assertThat(actualBooks).isNotNull()
                .hasSize(expectedBooksSize)
                .contains(expectedBook);
    }

    @DisplayName("должен корректно находить книги по названию")
    @Test
    void shouldCorrectGetSomeBooksByName() {
        final int expectedListSize = 2;
        final String expectedPartOfBookName = "стров";
        Page<Book> actualBooks = bookRepositoryJdbc.getAllByLikeBookName(expectedPartOfBookName, PageRequest.of(0, 10));
        assertAll(() -> assertThat(actualBooks).isNotNull(),
                () -> assertThat(actualBooks.getContent()).hasSize(expectedListSize)
                        .matches(booksList -> booksList.stream()
                                .allMatch(book -> book.getName().contains(expectedPartOfBookName))));
    }

    @DisplayName("должен корректно находить книги по имени автора")
    @Test
    void shouldCorrectGetAllByAuthorName() {
        final int expectedListSize = 3;
        final String expectedPartOfAuthorName = "Струг";
        final Book expectedBook = getBook();
        Page<Book> actualBooks = bookRepositoryJdbc.getAllByLikeAuthorName(expectedPartOfAuthorName, PageRequest.of(0, 10));
        assertAll(() -> assertThat(actualBooks).isNotNull()
                        .hasSize(expectedListSize)
                        .contains(expectedBook),
                () -> assertThat(actualBooks.getContent())
                        .matches(bookList -> bookList.stream()
                                .allMatch(book -> book.getAuthors().stream()
                                        .allMatch(author -> author.getName().contains(expectedPartOfAuthorName))
                                )));
    }

    @DisplayName("должен корректно находить книги по названию жанра (части названия жанра)")
    @Test
    void shouldCorrectGetAllByGenreName() {
        final int expectedListSize = 3;
        final String expectedPartOfGenreName = "Фант";
        final Book expectedBook = getBook();
        Page<Book> actualBooks = bookRepositoryJdbc.getAllByLikeGenreName(expectedPartOfGenreName, PageRequest.of(0, 10));
        assertAll(() -> assertThat(actualBooks).isNotNull()
                        .hasSize(expectedListSize)
                        .contains(expectedBook),
                () -> assertThat(actualBooks.getContent())
                        .matches(bookList -> bookList.stream()
                                .allMatch(book -> book.getGenre().getName().contains(expectedPartOfGenreName))));
    }

    @DisplayName("должен корректно находить книги по автору, названию жанра и названию книги.")
    @Test
    void shouldCorrectGetAllByAuthorNameAndLikeGenreNameAndLikeBookName() {
        final int expectedListSize = 1;
        final String expectedPartOfAuthorName = "Стругацкий";
        final String expectedPartOfGenreName = "Фантастик";
        final Book expectedBook = getBook();
        Page<Book> actualBooks = bookRepositoryJdbc.getAllByLikeAuthorNameAndLikeGenreNameAndLikeBookName(
                expectedPartOfAuthorName,
                expectedPartOfGenreName,
                EXPECTED_BOOK_NAME,
                PageRequest.of(0, 10));

        assertAll(() -> assertThat(actualBooks).isNotNull()
                        .hasSize(expectedListSize)
                        .contains(expectedBook),
                () -> assertThat(actualBooks.getContent())
                        .matches(bookList -> bookList.stream()
                                .allMatch(book -> {
                                    return book.getName().equals(EXPECTED_BOOK_NAME) &&
                                            book.getGenre().getName().contains(expectedPartOfGenreName);
                                })));
    }

    @DisplayName("должен корректно находить книги по автору и названию книги")
    @Test
    void shouldCorrectGetAllByLikeAuthorNameAndBookName() {
        final int expectedListSize = 1;
        final String expectedPartOfAuthorName = "Струг";
        final Book expectedBook = getBook();
        Page<Book> actualBooks = bookRepositoryJdbc.getAllByLikeAuthorNameAndLikeBookName(expectedPartOfAuthorName,
                EXPECTED_BOOK_NAME,
                PageRequest.of(0, 10));

        assertAll(() -> assertThat(actualBooks).isNotNull()
                        .hasSize(expectedListSize)
                        .contains(expectedBook),
                () -> assertThat(actualBooks.getContent())
                        .matches(bookList -> bookList.stream()
                                .allMatch(book -> book.getAuthors().stream()
                                        .allMatch(author -> author.getName().contains(expectedPartOfAuthorName)))),
                () -> assertThat(actualBooks.getContent())
                        .matches(bookList -> bookList.stream()
                                .allMatch(book -> book.getName().contains(EXPECTED_BOOK_NAME))));
    }

    @DisplayName("должен корректно находить книги по автору и названию жанра")
    @Test
    void shouldGetAllByLikeAuthorNameAndLikeGenreName() {
        final String expectedPartOfAuthorName = "Струг";
        final String expectedPartOfGenreName = "Фант";
        Page<Book> actualBooks = bookRepositoryJdbc.getAllByLikeAuthorNameAndLikeGenreName(expectedPartOfAuthorName,
                expectedPartOfGenreName,
                PageRequest.of(0, 10));

        assertAll(() -> assertThat(actualBooks).isNotNull(),
                () -> assertThat(actualBooks.getContent())
                        .matches(bookList -> bookList.stream()
                                .allMatch(book -> book.getAuthors().stream()
                                        .allMatch(author -> author.getName().contains(expectedPartOfAuthorName)))),
                () -> assertThat(actualBooks.getContent())
                        .matches(bookList -> bookList.stream()
                                .allMatch(book -> book.getGenre().getName().contains(expectedPartOfGenreName))));
    }

    @DisplayName("должен корректно находить книги по названию жанра и названию книги")
    @Test
    void shouldGetAllByLikeGenreNameAndBookName() {
        final String expectedPartOfGenreName = "Фант";
        final String expectedPartOfBookName = "быть";
        final Book expectedBook = getBook();
        Page<Book> actualBooks = bookRepositoryJdbc.getAllByLikeGenreNameAndLikeBookName(expectedPartOfGenreName,
                expectedPartOfBookName,
                PageRequest.of(0, 10));

        assertAll(() -> assertThat(actualBooks).isNotNull().contains(expectedBook),
                () -> assertThat(actualBooks.getContent())
                        .matches(bookList -> bookList.stream()
                                .allMatch(book -> book.getGenre().getName().contains(expectedPartOfGenreName) &&
                                        book.getName().contains(expectedPartOfBookName))));
    }

    @DisplayName("должен корректно находить первую книгу по isbn")
    @Test
    void shouldGetByIsbn() {
        final String expectedIsbn = "1234";
        final Book expectedBook = new Book(2L, "Гадкие лебеди", "1234", new Genre(1L, EXPECTED_GENRE_NAME),
                List.of(new Author(4L, EXPECTED_AUTHOR_NAME_1, null, EXPECTED_BIRTHDAY_AUTHOR_1),
                        new Author(5L, EXPECTED_AUTHOR_NAME_2, null, EXPECTED_BIRTHDAY_AUTHOR_2)));
        final Book actualBook = bookRepositoryJdbc.getByIsbn(expectedIsbn);

        assertThat(actualBook)
                .isNotNull()
                .isEqualTo(expectedBook)
                .matches(book -> book.getIsbn().equals(expectedIsbn));
    }

    @DisplayName("должен выбрасывать исключение если книги с таким isbn не существует")
    @Test
    void shouldThrowExceptionIfBookWithIsbnNotExists() {
        final String isbn = "4321";
        assertThrows(BookNotFoundException.class, () -> bookRepositoryJdbc.getByIsbn(isbn));
    }

    @DisplayName("должен корректно находить все книги автора")
    @Test
    void shouldGetAllByAuthor() {
        final int expectedBooksSize = 3;
        final Book expectedBook = getBook();
        final Author expectedAuthor = new Author(4L, EXPECTED_AUTHOR_NAME_1, null, EXPECTED_BIRTHDAY_AUTHOR_1);
        final List<Book> actualBooks = bookRepositoryJdbc.getAllByAuthor(expectedAuthor);

        assertAll(() -> assertThat(actualBooks).isNotNull().hasSize(expectedBooksSize).contains(expectedBook),
                () -> assertThat(actualBooks).allMatch(book -> book.getAuthors().contains(expectedAuthor)));
    }

    @DisplayName("должен корректно проверять существует ли книга с заданным id")
    @ParameterizedTest
    @CsvSource(value = {"1, true", "10, false"})
    void shouldCheckExistsBookById(Long id, Boolean expectedBookExists) {
        final Boolean actualBookExists = bookRepositoryJdbc.isExistsById(id);
        assertEquals(expectedBookExists, actualBookExists);
    }

    @DisplayName("должен корректно проверять существует ли книга с заданным id")
    @ParameterizedTest
    @CsvSource(value = {"1234, true", "4321, false"})
    void shouldCheckExistsBookByIsbn(String isbn, Boolean expectedBookExists) {
        final Boolean actualBookExists = bookRepositoryJdbc.isExistsByIsbn(isbn);
        assertEquals(expectedBookExists, actualBookExists);
    }
}