package ru.otus.spring.repositories;

import lombok.Getter;

import java.util.HashMap;

/**
 * Конструктор запросов для работы с авторами. (очень простой велосипед :))
 */
class BookQueryBuilder {
    private final StringBuffer selectStart = new StringBuffer("SELECT b FROM Book b ");
    private final StringBuffer selectCountStart = new StringBuffer("SELECT count(*) FROM Book b ");
    private final StringBuffer sql = new StringBuffer();
    @Getter
    private final HashMap<String, Object> parameters = new HashMap<>();

    private BookQueryBuilder() {
    }

    static BookQueryBuilder builder() {
        BookQueryBuilder builder = new BookQueryBuilder();
        return builder;
    }

    String build() {
        return selectStart.toString() + (parameters.size() > 0 ? "WHERE " : "") + sql.toString();
    }

    String buildCountQuery() {
        return selectCountStart.toString() + (parameters.size() > 0 ? "WHERE " : "") + sql.toString();
    }

    private void and() {
        if (parameters.size() > 0)
            sql.append("AND ");
    }

    public BookQueryBuilder likeAuthorName(String authorName) {
        and();
        String authorJoin = " INNER JOIN b.authors a ";
        selectCountStart.append(authorJoin);
        selectStart.append(authorJoin);
        sql.append("a.name like :author ");
        parameters.put("author", "%" + authorName + "%");
        return this;
    }

    BookQueryBuilder likeBookName(String bookName) {
        and();
        sql.append(" b.name like :bookName ");
        parameters.put("bookName", "%" + bookName + "%");
        return this;
    }

    BookQueryBuilder likeGenreName(String genreName) {
        and();
        String genreJoin = " INNER JOIN b.genre g ";
        selectCountStart.append(genreJoin);
        selectStart.append(genreJoin);
        sql.append(" g.name like :genreName ");
        parameters.put("genreName", "%" + genreName + "%");
        return this;
    }

    BookQueryBuilder orderBy() {
        sql.append(" ORDER BY b.name");
        return this;
    }
}

