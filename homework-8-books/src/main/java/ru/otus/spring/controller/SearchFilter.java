package ru.otus.spring.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Критерии поиска
 */
@AllArgsConstructor
@Getter
@Builder
public class SearchFilter {
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
        return authorName == null || getAuthorName().isEmpty();
    }

    public Boolean isBookNameEmpty() {
        return bookName == null || bookName.isEmpty();
    }

    public Boolean isGenreNameEmpty() {
        return genreName == null || genreName.isEmpty();
    }
}
