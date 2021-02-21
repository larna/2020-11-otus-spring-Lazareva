package ru.otus.spring.repositories.authors;

import lombok.Getter;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Конструктор запросов для работы с авторами. (очень простой велосипед :))
 * Можно заменить на JOOQ или QueryDsl, что наверное будет более правильнее.
 */
class AuthorQueryBuilder {
    private static final String ID_DB = "id";
    private static final String NAME_DB = "name";
    private static final String REAL_NAME_DB = "real_name";
    private static final String BIRTHDAY_DB = "birthday";

    private final StringBuffer selectStart = new StringBuffer();
    private final StringBuffer sql = new StringBuffer();
    @Getter
    private final MapSqlParameterSource parameters = new MapSqlParameterSource();

    private AuthorQueryBuilder() {
    }

    static Map<String, String> getMappingObjectToDb() {
        return Map.of("id", ID_DB, "name", NAME_DB, "realName", REAL_NAME_DB, "birthday", BIRTHDAY_DB);
    }

    static AuthorQueryBuilder select() {
        AuthorQueryBuilder builder = new AuthorQueryBuilder();
        builder.selectStart.append("SELECT ");
        return builder;
    }

    static AuthorQueryBuilder insertAuthor(Author author) {
        AuthorQueryBuilder builder = new AuthorQueryBuilder();
        builder.buildInsert(author);
        return builder;
    }

    static AuthorQueryBuilder updateAuthor(Author author) {
        AuthorQueryBuilder builder = new AuthorQueryBuilder();
        builder.buildUpdate(author);
        return builder;
    }

    static AuthorQueryBuilder deleteFromAuthors() {
        AuthorQueryBuilder builder = new AuthorQueryBuilder();
        builder.sql.append("DELETE ");
        builder.fromAuthors();
        return builder;
    }

    String build() {
        return selectStart.toString() + sql.toString();
    }

    String buildCountQuery() {
        return "SELECT count(*) " + sql.toString();
    }

    private void buildInsert(Author author) {
        sql.append("INSERT INTO authors(" + NAME_DB + ", " + REAL_NAME_DB + ", " + BIRTHDAY_DB + ") VALUES (:name, :real_name, :birthday)");
        parameters.addValue("name", author.getName());
        parameters.addValue("real_name", author.getRealName());
        parameters.addValue("birthday", author.getBirthday());
    }

    private void buildUpdate(Author author) {
        sql.append("UPDATE authors SET " + NAME_DB + "=:name, " + REAL_NAME_DB + "=:real_name, " + BIRTHDAY_DB + "=:birthday WHERE id=:id");
        parameters.addValue("id", author.getId());
        parameters.addValue("name", author.getName());
        parameters.addValue("real_name", author.getRealName());
        parameters.addValue("birthday", author.getBirthday());
    }

    AuthorQueryBuilder all() {
        selectStart.append(ID_DB + ", " + NAME_DB + ", " + REAL_NAME_DB + ", " + BIRTHDAY_DB+" ");
        return this;
    }

    AuthorQueryBuilder existsAuthorById(Long id) {
        sql.append("EXISTS(SELECT 1 FROM authors WHERE " + ID_DB + "=:id) ");
        parameters.addValue("id", id);
        return this;
    }

    AuthorQueryBuilder fromAuthors() {
        sql.append("FROM authors ");
        return this;
    }

    AuthorQueryBuilder innerJoinBooksAuthors() {
        sql.append("INNER JOIN books_authors as ba ON( ba.author_id=authors." + ID_DB + ") ");
        return this;
    }

    AuthorQueryBuilder innerJoinBooks() {
        sql.append("INNER JOIN books ON( ba.book_id=books.id) ");
        return this;
    }

    AuthorQueryBuilder where() {
        sql.append("WHERE ");
        return this;
    }

    private void and() {
        if (parameters.getValues().size() > 0)
            sql.append("AND ");
    }

    AuthorQueryBuilder equalsGenreId(Long genreId) {
        and();
        sql.append("books.genre_id=:genreId ");
        parameters.addValue("genreId", genreId);
        return this;
    }

    AuthorQueryBuilder equalsName(String name) {
        and();
        sql.append("authors." + NAME_DB + "=:name ");
        parameters.addValue("name", name);
        return this;
    }

    AuthorQueryBuilder equalsRealName(String realName) {
        and();
        if(realName == null){
            sql.append("authors." + REAL_NAME_DB + " IS NULL ");
            return this;
        }
        sql.append("authors." + REAL_NAME_DB + "=:realName ");
        parameters.addValue("realName", realName);
        return this;
    }

    AuthorQueryBuilder equalsBirthday(LocalDate birthday) {
        and();
        if(birthday == null){
            sql.append("authors." + BIRTHDAY_DB + " IS NULL ");
            return this;
        }
        sql.append("authors." + BIRTHDAY_DB + "=:birthday ");
        parameters.addValue("birthday", birthday);
        return this;
    }

    public AuthorQueryBuilder likeName(String name) {
        and();
        sql.append("authors." + NAME_DB + " like :name ");
        parameters.addValue("name", "%" + name + "%");
        return this;
    }

    AuthorQueryBuilder likeBookName(String bookName) {
        and();
        sql.append("books.name like :bookName ");
        parameters.addValue("bookName", bookName);
        return this;
    }

    AuthorQueryBuilder likeGenreName(String genreName) {
        and();
        sql.append("books.genre_id IN (SELECT id FROM genres WHERE name LIKE :genreName) ");
        parameters.addValue("genreName", genreName);
        return this;
    }

    AuthorQueryBuilder equalsId(Long id) {
        and();
        sql.append("authors." + ID_DB + "=:id ");
        parameters.addValue("id", id);
        return this;
    }
    public AuthorQueryBuilder equalsListId(List<Long> authorIdList) {
        and();
        sql.append("authors." + ID_DB + " IN (:idList) ");
        parameters.addValue("idList", authorIdList);
        return this;
    }



    AuthorQueryBuilder orderBy(Sort sort) {
        if (sort == null)
            return this;
        String orderBy = sort.stream()
                .map(s -> {
                    String nameInDb = getMappingObjectToDb().get(s.getProperty());
                    return nameInDb == null ? null : nameInDb + " " + (s.isAscending() ? "ASC " : "DESC ");
                })
                .filter(Objects::nonNull)
                .reduce((s1, s2) -> s1 + ", " + s2).orElse("");
        if (orderBy.isEmpty())
            return this;

        sql.append("ORDER BY ").append(orderBy);
        return this;
    }

    AuthorQueryBuilder limit(Integer limit) {
        sql.append("LIMIT :limit ");
        parameters.addValue("limit", limit);
        return this;
    }

    AuthorQueryBuilder offset(Long offset) {
        sql.append("OFFSET :offset ");
        parameters.addValue("offset", offset);
        return this;
    }

    public AuthorQueryBuilder authorsForBookIn(List<Book> books) {
        if(books == null || books.isEmpty())
            return this;

        List<Long> booksId = books.stream().map(Book::getId).collect(Collectors.toList());
        and();
        sql.append("authors.id IN (SELECT ba.author_id FROM books_authors ba WHERE ba.book_id IN (:booksId)) ");
        parameters.addValue("booksId", booksId);
        return this;
    }
}

