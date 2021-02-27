package ru.otus.spring.repositories;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DisplayName("Класс BookRepositoryJpa")
@DataJpaTest
@Import({BookRepositoryJpa.class})
class BookRepositoryJpaTest {
    private static final String EXPECTED_BOOK_NAME = "Остров сокровищ";
    private static final String EXPECTED_NEW_BOOK_NAME = "Тест";
    private static final String EXPECTED_ISBN = "ISBN-1234";

    private static final Genre EXPECTED_GENRE = Genre.builder().id(3L).name("Приключения").build();
    private static final Author EXPECTED_AUTHOR = Author.builder().id(6L).name("Роберт Стивенсон")
            .birthday(LocalDate.of(1850, 11, 13)).build();

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookRepositoryJpa bookRepositoryJpa;

    @DisplayName("Должен сохранять книги")
    @Test
    void shouldSaveNewBook() {
        Long expectedId = 6L;
        String expectedIsbn = "Test-1213";
        Book newBook = Book.builder().name(EXPECTED_NEW_BOOK_NAME).isbn(expectedIsbn)
                .genre(EXPECTED_GENRE).authors(List.of(EXPECTED_AUTHOR)).build();
        bookRepositoryJpa.save(newBook);
        Book actual = em.find(Book.class, newBook.getId());
        assertThat(actual)
                .isNotNull()
                .isEqualTo(newBook)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", EXPECTED_NEW_BOOK_NAME)
                .hasFieldOrPropertyWithValue("isbn", expectedIsbn)
                .hasFieldOrPropertyWithValue("genre", EXPECTED_GENRE)
                .matches(book -> book.getAuthors().contains(EXPECTED_AUTHOR));
    }

    @DisplayName("Должен сохранять изменения книги")
    @Test
    void shouldUpdateBook() {
        Long expectedId = 5L;
        Book updatedBook = Book.builder().id(expectedId).name(EXPECTED_NEW_BOOK_NAME).isbn(EXPECTED_ISBN)
                .genre(EXPECTED_GENRE).authors(List.of(EXPECTED_AUTHOR)).build();
        Comment comment = Comment.builder().id(1L).description("Comment").book(updatedBook).build();
        updatedBook.setComments(List.of(comment));

        bookRepositoryJpa.save(updatedBook);
        Book actual = em.find(Book.class, updatedBook.getId());

        assertThat(actual)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", EXPECTED_NEW_BOOK_NAME)
                .hasFieldOrPropertyWithValue("isbn", EXPECTED_ISBN)
                .hasFieldOrPropertyWithValue("genre", EXPECTED_GENRE)
                .matches(book -> book.getAuthors().contains(EXPECTED_AUTHOR));
    }

    @DisplayName("Должен изменять название книги")
    @Test
    void shouldUpdateBookName() {
        Long expectedId = 5L;
        Book actual = bookRepositoryJpa.updateBookName(expectedId, EXPECTED_NEW_BOOK_NAME);
        assertThat(actual)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", EXPECTED_NEW_BOOK_NAME)
                .hasFieldOrPropertyWithValue("isbn", EXPECTED_ISBN)
                .hasNoNullFieldsOrPropertiesExcept("isbn", "comments")
                .hasFieldOrPropertyWithValue("genre", EXPECTED_GENRE)
                .matches(book -> book.getAuthors().contains(EXPECTED_AUTHOR));
    }

    @DisplayName("Должен удалять книгу с комментариями")
    @Test
    void shouldDeleteBookWithComments() {
        Long expectedId = 5L, commentId1 = 1l, commentId2 = 2L;
        Book deletedBook = bookRepositoryJpa.findById(expectedId).get();
        Comment comment1 = em.find(Comment.class, commentId1);
        Comment comment2 = em.find(Comment.class, commentId2);

        assertThat(comment1).isNotNull();
        assertThat(comment2).isNotNull();
        assertThat(deletedBook).isNotNull();

        bookRepositoryJpa.delete(deletedBook);

        Book actual = em.find(Book.class, expectedId);
        comment1 = em.find(Comment.class, commentId1);
        comment2 = em.find(Comment.class, commentId2);

        assertThat(actual).isNull();
        assertThat(comment1).isNull();
        assertThat(comment2).isNull();

    }

    @DisplayName("Должен проверять есть ли книга c таким id")
    @ParameterizedTest
    @CsvSource(value = {"true,1", "false,10"})
    void shouldExistsBookById(Boolean expected, Long id) {
        Boolean actual = bookRepositoryJpa.existsById(id);
        assertEquals(expected, actual);
    }

    @DisplayName("Должен проверять есть ли книга c таким isbn")
    @ParameterizedTest
    @CsvSource(value = {"true,1234", "false,12345"})
    void shouldExistsBookByIsbn(Boolean expected, String isbn) {
        Boolean actual = bookRepositoryJpa.existsBookByIsbn(isbn);
        assertEquals(expected, actual);
    }

    @DisplayName("Должен возвращать книгу по id")
    @Test
    void shouldFindById() {
        Long expectedId = 5L;
        int expectedCommentSize = 2;
        Book actual = bookRepositoryJpa.findById(expectedId).get();
        assertThat(actual)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", EXPECTED_BOOK_NAME)
                .hasFieldOrPropertyWithValue("isbn", EXPECTED_ISBN)
                .hasNoNullFieldsOrPropertiesExcept("comments")
                .hasFieldOrPropertyWithValue("genre", EXPECTED_GENRE)
                .matches(book -> book.getAuthors().contains(EXPECTED_AUTHOR))
                .matches(book -> book.getComments().size() == expectedCommentSize);
    }

    @DisplayName("Должен возвращать книгу по isbn")
    @Test
    void shouldFindByIsbn() {
        Long expectedId = 5L;
        int expectedCommentSize = 2;
        Book actual = bookRepositoryJpa.findBookByIsbn(EXPECTED_ISBN);
        assertThat(actual)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", EXPECTED_BOOK_NAME)
                .hasFieldOrPropertyWithValue("isbn", EXPECTED_ISBN)
                .hasNoNullFieldsOrPropertiesExcept("comments")
                .hasFieldOrPropertyWithValue("genre", EXPECTED_GENRE)
                .matches(book -> book.getAuthors().contains(EXPECTED_AUTHOR))
                .matches(book -> book.getComments().size() == expectedCommentSize);
    }

    @DisplayName("Должен возвращать книги для автора")
    @Test
    void shouldFindAllByAuthorsIs() {
        Long expectedId = 4L;
        int expectedSize = 3;
        Author author = em.find(Author.class, expectedId);
        List<Book> actual = bookRepositoryJpa.findAllByAuthorsIs(author);
        assertThat(actual)
                .isNotNull()
                .hasSize(expectedSize);
    }

    @DisplayName("Должен возвращать книги для автора")
    @Test
    void shouldFindAllByGenre() {
        Long expectedId = 1L;
        int expectedSize = 3;
        Genre genre = em.find(Genre.class, expectedId);
        List<Book> actual = bookRepositoryJpa.findAllByGenre(genre);
        assertThat(actual)
                .isNotNull()
                .hasSize(expectedSize);
    }

    @DisplayName("Должен возвращать все книги")
    @Test
    void findAll() {
        int expectedQueryCount = 2;
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().clear();
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        int expectedSize = 5;
        Page<Book> actual = bookRepositoryJpa.findAll(PageRequest.of(0, 10));
        assertThat(actual)
                .isNotNull()
                .hasSize(expectedSize);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(expectedQueryCount);
    }
    @DisplayName("Должен возвращать все книги")
    @Test
    void shouldFindAll() {
        int expectedQueryCount = 1;
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().clear();
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        int expectedSize = 5;
        List<Book> actual = bookRepositoryJpa.findAll();
        assertThat(actual)
                .isNotNull()
                .hasSize(expectedSize);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(expectedQueryCount);
    }

    @DisplayName("Должен возвращать все книги")
    @ParameterizedTest
    @CsvSource(value = {"3,Струг,,", "0, Струг,Приключения,", "3,Струг,Фант,", "1,Струг,Фант,стров", "2,,,стров"})
    void testFindAll(Integer expectedCount, String authorName, String genreName, String bookName) {
        SearchFilter filter = new SearchFilter(authorName, genreName, bookName);
        Page<Book> books = bookRepositoryJpa.findAll(new BookSearchSpecification(filter), PageRequest.of(0, 10));
        assertThat(books).isNotNull().hasSize(expectedCount);
    }

    @DisplayName("Должен возвращать книгу по id")
    @Test
    void findBookById() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        Long expectedId = 5L;
        Long expectedLoadCollection = 2L;
        int expectedCommentSize = 2;
        int expectedQueryCount = 4;
        Book actual = bookRepositoryJpa.findById(expectedId).get();
        assertThat(actual)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", EXPECTED_BOOK_NAME)
                .hasFieldOrPropertyWithValue("isbn", EXPECTED_ISBN)
                .hasNoNullFieldsOrPropertiesExcept("comments")
                .hasFieldOrPropertyWithValue("genre", EXPECTED_GENRE)
                .matches(book -> book.getAuthors().contains(EXPECTED_AUTHOR))
                .matches(book -> book.getComments().size() == expectedCommentSize);
        assertThat(sessionFactory.getStatistics().getCollectionLoadCount()).isEqualTo(expectedLoadCollection);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(expectedQueryCount);
    }
}