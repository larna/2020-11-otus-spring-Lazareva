package ru.otus.spring.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.MongoConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("test")
@DataMongoTest
@ComponentScan({"ru.otus.spring.repositories"})
@ContextConfiguration(classes = {MongoConfig.class})
@Import(BookCascadeUpdateEventsListener.class)
@DisplayName("BookRepository при наличие BookCascadeUpdateEventsListener")
class BookCascadeUpdateEventsListenerTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    @DisplayName("Должен обновлять книгу в комментариях к книге")
    @Test
    public void shouldRemoveCommentsWithRemoveBook() {
        final String expectedBookName = "bookName";
        Book book = mongoTemplate.findOne(new Query(Criteria.where("name").is("Трудно быть богом")), Book.class);
        book.setName(expectedBookName);
        bookRepository.save(book);
        Book bookUpdated = mongoTemplate.findOne(new Query(Criteria.where("name").is("Трудно быть богом")), Book.class);
        List<Comment> commentsAfterUpdatedBook = mongoTemplate.find(new Query(Criteria.where("book").is(book)), Comment.class);
        assertAll(() -> assertNull(bookUpdated),
                () -> assertThat(commentsAfterUpdatedBook)
                        .allMatch(comment -> comment.getBook().getName().equals(expectedBookName)));
    }
}