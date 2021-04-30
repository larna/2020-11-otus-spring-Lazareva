package ru.otus.spring.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Критерии поиска
 */
@Builder
@Getter
public class SearchFilter {
    public SearchFilter(String authorName, String genreName, String bookName) {
        this.authorName = authorName;
        this.genreName = genreName;
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getGenreName() {
        return genreName;
    }

    public String getBookName() {
        return bookName;
    }

    /**
     * Имя автора
     */
    private final String authorName;
    /**
     * Название жанра
     */
    private final String genreName;
    /**
     * Название книги
     */
    private final String bookName;

    public Boolean isEmpty() {
        return isAuthorNameEmpty() && isGenreNameEmpty() && isBookNameEmpty();
    }

    public Boolean isAuthorNameEmpty() {
        return authorName == null || authorName.isEmpty();
    }

    public Boolean isBookNameEmpty() {
        return bookName == null || bookName.isEmpty();
    }

    public Boolean isGenreNameEmpty() {
        return genreName == null || genreName.isEmpty();
    }
}
