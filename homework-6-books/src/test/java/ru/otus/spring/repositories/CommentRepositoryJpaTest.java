package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DisplayName("Класс CommentRepositoryJpa")
@DataJpaTest
@Import({CommentRepositoryJpa.class})
class CommentRepositoryJpaTest {
    private static final String EXPECTED_COMMENT = "комментарий 1";
    @Autowired
    private TestEntityManager em;

    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;

    @DisplayName("Должен сохранять комментарий")
    @Test
    void shouldSaveNewComment() {
        Long expectedId = 3L;
        Book book = em.find(Book.class, 1L);
        Comment newComment = Comment.builder().description(EXPECTED_COMMENT).book(book).build();
        commentRepositoryJpa.save(newComment);
        Comment actual = em.find(Comment.class, newComment.getId());
        assertThat(actual)
                .isNotNull()
                .isEqualTo(newComment)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("description", EXPECTED_COMMENT);
    }

    @DisplayName("Должен возвращать комментарий по id")
    @Test
    void shouldFindById() {
        Long expectedId = 1L;
        Comment expected = em.find(Comment.class, expectedId);
        Comment actual = commentRepositoryJpa.findById(expectedId).get();
        assertThat(actual)
                .isNotNull()
                .isEqualTo(expected)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("description", EXPECTED_COMMENT);
    }

    @DisplayName("Должен проверять существование комментария по id")
    @ParameterizedTest
    @CsvSource(value = {"true,1", "false, 10"})
    void shouldCheckExistsById(Boolean expected, Long id) {
        Boolean actual = commentRepositoryJpa.existsById(id);
        assertEquals(expected, actual);
    }

    @DisplayName("Должен удалять комментарий по id")
    @Test
    void shouldCorrectDelete() {
        Long expectedId = 1L;
        Comment comment = em.find(Comment.class, expectedId);
        commentRepositoryJpa.delete(comment);
        Comment expected = em.find(Comment.class, expectedId);
        assertThat(expected).isNull();
    }

    @DisplayName("Должен возвращать все комментарии для id книги")
    @Test
    void findAllByBook_Id() {
        int expectedSize = 2;
        Long expectedBookId = 5L;
        List<Comment> actual = commentRepositoryJpa.findAllByBook_Id(expectedBookId);
        assertThat(actual)
                .isNotNull().hasSize(expectedSize);
    }

    @DisplayName("Должен возвращать все комментарии")
    @Test
    void findAll() {
        int expectedSize = 2;
        List<Comment> actual = commentRepositoryJpa.findAll();
        assertThat(actual)
                .isNotNull().hasSize(expectedSize);


    }
}