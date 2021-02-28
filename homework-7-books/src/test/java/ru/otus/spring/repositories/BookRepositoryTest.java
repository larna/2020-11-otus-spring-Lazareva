package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DisplayName("Класс BookRepository")
@DataJpaTest
class BookRepositoryTest {
    @Autowired
    TestEntityManager em;
    @Autowired
    private BookRepository repository;

    @DisplayName("Должен изменять название книги")
    @Test
    void shouldUpdateBookNameById() {
        final Long bookId = 5L;
        final String expectedBookName = "Test";

        repository.updateBookName(bookId, expectedBookName);
        Book actual = em.find(Book.class, bookId);
        assertThat(actual).isNotNull().hasFieldOrPropertyWithValue("name",expectedBookName);
    }
    @DisplayName("Должен выбирать книги согласно фильтру")
    @ParameterizedTest
    @CsvSource(value = {"3,Струг,,", "0, Струг,Приключения,", "3,Струг,Фант,", "1,Струг,Фант,стров", "2,,,стров"})
    void shouldFindAccordingFilter(Integer expectedCount, String authorName, String genreName, String bookName) {
        SearchFilter filter = new SearchFilter(authorName, genreName, bookName);
        Page<Book> books = repository.findAll(new BookSearchSpecification(filter), PageRequest.of(0, 10));

        assertThat(books).isNotNull().hasSize(expectedCount);
    }
}