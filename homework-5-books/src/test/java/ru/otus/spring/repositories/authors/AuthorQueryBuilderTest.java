package ru.otus.spring.repositories.authors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс AuthorQueryBuilder")
class AuthorQueryBuilderTest {
    @DisplayName("Должен корректно строить запрос insert")
    @Test
    void shouldBuildCorrectInsertQuery() {
        Author author = new Author(null, "Test", "Test", LocalDate.now().minusYears(20));
        AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.insertAuthor(author);
        final Integer expectedParametersSize = 3;
        final String expected = "INSERT INTO authors(name, real_name, birthday) VALUES (:name, :real_name, :birthday)";

        final String actual = queryBuilder.build();
        assertAll(() -> assertEquals(expected, actual),
                () -> assertEquals(expectedParametersSize, queryBuilder.getParameters().getParameterNames().length));
    }
    @DisplayName("Должен корректно строить запрос update")
    @Test
    void shouldBuildCorrectUpdateQuery() {
        Author author = new Author(null, "Test", "Test", LocalDate.now().minusYears(20));
        AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.updateAuthor(author);
        final Integer expectedParametersSize = 4;
        final String expected = "UPDATE authors SET name=:name, real_name=:real_name, birthday=:birthday WHERE id=:id";

        final String actual = queryBuilder.build();
        assertAll(() -> assertEquals(expected, actual),
                () -> assertEquals(expectedParametersSize, queryBuilder.getParameters().getParameterNames().length));
    }
    @DisplayName("Должен корректно строить запрос delete")
    @Test
    void shouldBuildCorrectDeleteQuery() {
        AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.deleteFromAuthors().where().equalsId(1L);
        final Integer expectedParametersSize = 1;
        final String expected = "DELETE FROM authors WHERE authors.id=:id ";

        final String actual = queryBuilder.build();
        assertAll(() -> assertEquals(expected, actual),
                () -> assertEquals(expectedParametersSize, queryBuilder.getParameters().getParameterNames().length));
    }
    @DisplayName("Должен корректно строить запрос exists")
    @Test
    void shouldBuildCorrectExistsQuery() {
        AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.select().existsAuthorById(1L);
        final Integer expectedParametersSize = 1;
        final String expected = "SELECT EXISTS(SELECT 1 FROM authors WHERE id=:id) ";

        final String actual = queryBuilder.build();
        assertAll(() -> assertEquals(expected, actual),
                () -> assertEquals(expectedParametersSize, queryBuilder.getParameters().getParameterNames().length));
    }
    @DisplayName("Должен корректно строить запрос getById")
    @Test
    void shouldBuildCorrectGetByIdQuery() {
        AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.select().all().fromAuthors().where().equalsId(1L);
        final Integer expectedParametersSize = 1;
        final String expected = "SELECT id, name, real_name, birthday FROM authors WHERE authors.id=:id ";

        final String actual = queryBuilder.build();
        assertAll(() -> assertEquals(expected, actual),
                () -> assertEquals(expectedParametersSize, queryBuilder.getParameters().getParameterNames().length));
    }

    @DisplayName("Должен корректно строить запрос getAuthorByNameAndRealNameAndBirthday")
    @Test
    void shouldBuildCorrectGetAuthorByNameAndRealNameAndBirthdayQuery() {
        final AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.select().all().fromAuthors()
                .where().equalsName("Test").equalsRealName("Test").equalsBirthday(LocalDate.now().minusYears(20));

        final Integer expectedParametersSize = 3;
        final String expected = "SELECT id, name, real_name, birthday FROM authors WHERE authors.name=:name " +
                "AND authors.real_name=:realName AND authors.birthday=:birthday ";

        final String actual = queryBuilder.build();
        assertAll(() -> assertEquals(expected, actual),
                () -> assertEquals(expectedParametersSize, queryBuilder.getParameters().getParameterNames().length));
    }
    @DisplayName("Должен корректно строить запрос getAllByBooks")
    @Test
    void shouldBuildCorrectGetAllByBooksQuery() {
        final AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.select().all().fromAuthors().where()
                .authorsForBookIn(List.of(new Book(1L, "Test", null, new Genre(1L, "test"), List.of())));

        final Integer expectedParametersSize = 1;
        final String expected = "SELECT id, name, real_name, birthday FROM authors WHERE " +
                "authors.id IN (SELECT ba.author_id FROM books_authors ba WHERE ba.book_id IN (:booksId)) ";

        final String actual = queryBuilder.build();
        assertAll(() -> assertEquals(expected, actual),
                () -> assertEquals(expectedParametersSize, queryBuilder.getParameters().getParameterNames().length));
    }
    @DisplayName("Должен корректно строить запрос getAuthorByName")
    @Test
    void shouldBuildCorrectGetAuthorByNameQuery() {
        final AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.select().all().fromAuthors().where().likeName("Test");

        final Integer expectedParametersSize = 1;
        final String expected = "SELECT id, name, real_name, birthday FROM authors WHERE " +
                "authors.name like :name ";

        final String actual = queryBuilder.build();
        assertAll(() -> assertEquals(expected, actual),
                () -> assertEquals(expectedParametersSize, queryBuilder.getParameters().getParameterNames().length));
    }
    @DisplayName("Должен корректно строить запрос getAuthorByName")
    @Test
    void shouldBuildCorrectGetAllQuery() {
        final AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.select().all().fromAuthors();
        final String expectedCount = "SELECT count(*) FROM authors ";
        final String expected = "SELECT id, name, real_name, birthday FROM authors ORDER BY name ASC LIMIT :limit OFFSET :offset ";

        final String actualCount = queryBuilder.buildCountQuery();

        queryBuilder.orderBy(Sort.by("name")).limit(10).offset(0L);
        final String actual = queryBuilder.build();

        assertAll(() -> assertEquals(expectedCount, actualCount),
                () -> assertEquals(expected, actual));
    }
}