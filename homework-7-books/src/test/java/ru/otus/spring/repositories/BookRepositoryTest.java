package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DisplayName("Класс BookServiceImpl")
@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository repository;

    @DisplayName("Должен находить книгу с авторами и жанрами")
    @Test
    void shouldFindBookById() {
        Long bookId = 5L;
        Genre genre = Genre.builder().id(3L).name("Приключения").build();
        Author author = Author.builder().id(6L).name("Роберт Стивенсон").birthday(LocalDate.of(1850, 11, 13)).build();
        Book expected = Book.builder().id(bookId).name("Остров сокровищ").genre(genre).authors(List.of(author)).build();
        Book actual = repository.findBookById(bookId);
        assertAll(() -> assertEquals(expected, actual),
                ()->assertEquals(genre, actual.getGenre()),
                ()->assertTrue(actual.getAuthors().contains(author)));
    }

}