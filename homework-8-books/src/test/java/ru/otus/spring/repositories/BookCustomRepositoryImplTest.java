package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Book;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataMongoTest
@ComponentScan({"ru.otus.spring.repositories"})
@ContextConfiguration(classes = {MongoConfig.class})
@DisplayName("BookRepository")
class BookCustomRepositoryImplTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    @DisplayName("Ищет книги по фильтру")
    @ParameterizedTest
    @CsvSource(value = {",,стров,2", ",Фантастика,стров,1", "Струг,Фантастика,Обитаемый остров,1",
            ",Фантастика,,4", "Струг,,,3", "Струг,Приключения,остров, 0"})
    public void shouldCorrectFindBooksByFilter(String authorName, String genreName, String bookName, Integer expectedSize) {
        SearchFilter filter = new SearchFilter(authorName, genreName, bookName);
        Page<Book> books = bookRepository.findAllByFilter(filter, PageRequest.of(0, 10));
        assertAll(() -> assertNotNull(books),
                () -> assertEquals(expectedSize, books.getContent().size()));
    }

    @DisplayName("Должен выбирать книги из БД")
    @Test
    public void shouldCorrectFindBooksByFilter() {
        List<Book> books = bookRepository.findAll();
        assertAll(() -> assertNotNull(books),
                () -> assertFalse(books.isEmpty()));
    }
}