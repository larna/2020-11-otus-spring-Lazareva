package ru.otus.spring.repositories.books;

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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@DisplayName("Класс BookSearchSpecification")
@ExtendWith(MockitoExtension.class)
class BookSearchSpecificationTest {
    @Mock
    private BookRepository repository;

    @DisplayName("Корректно должен проверять удовлетворяют условия спецификации или нет")
    @ParameterizedTest
    @CsvSource(value = {"Test,,,true", ",Test,,true", ",,Test,true", "Test,Test,,true", ",,,false", "'','','',false"})
    void shouldCorrectIsSatisfiedBySpec(String authorName, String genreName, String bookName, Boolean expectedIsSatisfiedBy) {
        SearchFilter filter = new SearchFilter(authorName, genreName, bookName);
        BookSearchSpecification spec = new BookSearchSpecification(filter);
        assertEquals(expectedIsSatisfiedBy, spec.isSatisfiedBy());
    }

    @DisplayName("Должен для фильтров: имя автора, название жанра, название книги вызывать getAllByLikeAuthorNameAndLikeGenreNameAndLikeBookName метод репозитория")
    @Test
    void shouldInvokeGetAllByLikeAuthorNameAndLikeGenreNameAndLikeBookName() {
        SearchFilter filter = new SearchFilter("Test", "Test", "Test");
        BookSearchSpecification spec = new BookSearchSpecification(filter);
        Pageable pageable = PageRequest.of(0, 10);
        spec.execute(repository, pageable);
        Mockito.verify(repository, Mockito.times(1))
                .getAllByLikeAuthorNameAndLikeGenreNameAndLikeBookName(eq("Test"), eq("Test"), eq("Test"), any());
    }

    @DisplayName("Должен для фильтров: название жанра, название книги вызывать getAllByLikeGenreNameAndLikeBookName метод репозитория")
    @Test
    void shouldInvokeGetAllByLikeGenreNameAndLikeBookName() {
        SearchFilter filter = new SearchFilter(null, "Test", "Test");
        BookSearchSpecification spec = new BookSearchSpecification(filter);
        Pageable pageable = PageRequest.of(0, 10);
        spec.execute(repository, pageable);
        Mockito.verify(repository, Mockito.times(1))
                .getAllByLikeGenreNameAndLikeBookName(eq("Test"), eq("Test"), any());
    }
    @DisplayName("Должен для фильтров: имя автора, название жанра вызывать getAllByLikeAuthorNameAndLikeGenreName метод репозитория")
    @Test
    void shouldInvokeGetAllByLikeAuthorNameAndLikeGenreName() {
        SearchFilter filter = new SearchFilter("Test", "Test", null);
        BookSearchSpecification spec = new BookSearchSpecification(filter);
        Pageable pageable = PageRequest.of(0, 10);
        spec.execute(repository, pageable);
        Mockito.verify(repository, Mockito.times(1))
                .getAllByLikeAuthorNameAndLikeGenreName(eq("Test"), eq("Test"), any());
    }
    @DisplayName("Должен для фильтров: имя автора, название книги вызывать getAllByLikeAuthorNameAndLikeBookName метод репозитория")
    @Test
    void shouldInvokeGetAllByLikeAuthorNameAndLikeBookName() {
        SearchFilter filter = new SearchFilter("Test", null, "Test");
        BookSearchSpecification spec = new BookSearchSpecification(filter);
        Pageable pageable = PageRequest.of(0, 10);
        spec.execute(repository, pageable);
        Mockito.verify(repository, Mockito.times(1))
                .getAllByLikeAuthorNameAndLikeBookName(eq("Test"), eq("Test"), any());
    }
    @DisplayName("Должен для фильтров: название книги вызывать getAllByLikeBookName метод репозитория")
    @Test
    void shouldInvokeGetAllByLikeBookName() {
        SearchFilter filter = new SearchFilter(null, null, "Test");
        BookSearchSpecification spec = new BookSearchSpecification(filter);
        Pageable pageable = PageRequest.of(0, 10);
        spec.execute(repository, pageable);
        Mockito.verify(repository, Mockito.times(1))
                .getAllByLikeBookName(eq("Test"), any());
    }
    @DisplayName("Должен для фильтров: имя автора, название жанра вызывать getAllByLikeGenreName метод репозитория")
    @Test
    void shouldInvokeGetAllByLikeGenreName() {
        SearchFilter filter = new SearchFilter(null, "Test", null);
        BookSearchSpecification spec = new BookSearchSpecification(filter);
        Pageable pageable = PageRequest.of(0, 10);
        spec.execute(repository, pageable);
        Mockito.verify(repository, Mockito.times(1))
                .getAllByLikeGenreName(eq("Test"), any());
    }
    @DisplayName("Должен для фильтров: имя автора вызывать getAllByLikeAuthorName метод репозитория")
    @Test
    void shouldInvokeGetAllByLikeAuthorName() {
        SearchFilter filter = new SearchFilter("Test", null, null);
        BookSearchSpecification spec = new BookSearchSpecification(filter);
        Pageable pageable = PageRequest.of(0, 10);
        spec.execute(repository, pageable);
        Mockito.verify(repository, Mockito.times(1))
                .getAllByLikeAuthorName(eq("Test"), any());
    }
}