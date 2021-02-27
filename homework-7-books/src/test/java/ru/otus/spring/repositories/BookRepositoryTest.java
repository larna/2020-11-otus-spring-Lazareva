package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DisplayName("Класс BookRepository")
@DataJpaTest
class BookRepositoryTest {
    @Autowired
    TestEntityManager em;
    @Autowired
    private BookRepository repository;

    @DisplayName("Должен находить книгу с авторами и жанрами")
    @Test
    void shouldFindBookById() {
        Long bookId = 5L;
        Book expected = em.find(Book.class, bookId);
        Book actual = repository.findBookById(bookId);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }
    @DisplayName("Должен изменять название книги")
    @Test
    void shouldUpdateBookNameById() {
        final Long bookId = 5L;
        final String expectedBookName = "Test";

        repository.updateBookName(bookId, expectedBookName);
        Book actual = em.find(Book.class, bookId);
        assertThat(actual).isNotNull().hasFieldOrPropertyWithValue("name",expectedBookName);
    }

}