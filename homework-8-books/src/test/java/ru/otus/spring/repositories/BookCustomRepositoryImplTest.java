package ru.otus.spring.repositories;

import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import ru.otus.spring.config.ApplicationConfig;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@EnableConfigurationProperties
@DataMongoTest
@ComponentScan({"ru.otus.spring.repositories"})
class BookCustomRepositoryImplTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    public void init() {
        mongoTemplate.dropCollection(Author.class);
        mongoTemplate.dropCollection(Genre.class);
        mongoTemplate.dropCollection(Book.class);
        Author author1 = mongoTemplate.save(Author.builder().name("author1").build());
        Author author2 = mongoTemplate.save(Author.builder().name("author2").build());
        Author author3 = mongoTemplate.save(Author.builder().name("author3").build());
        Author author4 = mongoTemplate.save(Author.builder().name("OtherAuthor").build());

        Genre genre1 = mongoTemplate.save(Genre.builder().name("genre1").build());
        Genre genre2 = mongoTemplate.save(Genre.builder().name("genre2").build());
        Genre genre3 = mongoTemplate.save(Genre.builder().name("otherGenre").build());

        mongoTemplate.save(Book.builder().name("Test").genre(genre1).authors(List.of(author1, author2)).build());
        mongoTemplate.save(Book.builder().name("Test2").genre(genre2).authors(List.of(author3)).build());
        mongoTemplate.save(Book.builder().name("Other").genre(genre3).authors(List.of(author4)).build());
    }

    @ParameterizedTest
    @CsvSource(value = {",,Test,2", ",genre,Test,2", "author,genre,est2,1", "Author,Genre,Other,1",
            "uthor,enre,,3", "author,genre, Other, 0", "author,genre, Test, 2"})
    public void shouldCorrectFindBooksByFilter(String authorName, String genreName, String bookName, Integer expectedSize) {
        SearchFilter filter = new SearchFilter(authorName, genreName, bookName);
        Page<Book> books = bookRepository.findAllByFilter(filter, PageRequest.of(0, 10));
        assertAll(() -> assertNotNull(books),
                () -> assertEquals(expectedSize, books.getContent().size()));
    }
}