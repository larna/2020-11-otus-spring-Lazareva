package ru.otus.spring.repositories;

import lombok.Getter;

import java.util.HashMap;

/**
 * Конструктор запросов для работы с авторами. (очень простой велосипед :))
 */
class AuthorQueryBuilder {
    private final StringBuffer selectStart = new StringBuffer();
    private final StringBuffer sql = new StringBuffer();
    @Getter
    private final HashMap<String, Object> parameters = new HashMap<>();

    private AuthorQueryBuilder() {
    }

    static AuthorQueryBuilder builder() {
        AuthorQueryBuilder builder = new AuthorQueryBuilder();
        builder.selectStart.append("SELECT a FROM Author a ");
        return builder;
    }

    String build() {
        return selectStart.toString() + (parameters.size() > 0 ? "WHERE " : "") + sql.toString();
    }

    String buildCountQuery() {
        return "SELECT count(*) FROM Author a " + (parameters.size() > 0 ? "WHERE " : "") + sql.toString();
    }

    private void and() {
        if (parameters.size() > 0)
            sql.append("AND ");
    }

    public AuthorQueryBuilder likeName(String name) {
        and();
        sql.append("a.name like :author ");
        parameters.put("author", "%" + name + "%");
        return this;
    }

    AuthorQueryBuilder likeBookName(String bookName) {
        and();
        sql.append(" a IN (SELECT a FROM Book b WHERE b.name like :bookName )");
        parameters.put("bookName", "%" + bookName + "%");
        return this;
    }

    AuthorQueryBuilder likeGenreName(String genreName) {
        and();
        sql.append(" a IN (SELECT a FROM Book b INNER JOIN b.genre g WHERE g.name like :genreName )");
        parameters.put("genreName", "%" + genreName + "%");
        return this;
    }

    AuthorQueryBuilder orderBy() {
        sql.append(" ORDER BY a.name");
        return this;
    }
}

