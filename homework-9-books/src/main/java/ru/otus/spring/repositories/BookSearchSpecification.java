package ru.otus.spring.repositories;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.controller.SearchFilter;

@RequiredArgsConstructor
public class BookSearchSpecification {
    private final SearchFilter filter;

    public Boolean isSatisfied() {
        return !filter.isEmpty();
    }

    BookQueryBuilder execute() {
        BookQueryBuilder queryBuilder = BookQueryBuilder.builder();
        if (!filter.isAuthorNameEmpty())
            queryBuilder.likeAuthorName(filter.getAuthorName());
        if (!filter.isBookNameEmpty())
            queryBuilder.likeBookName(filter.getBookName());
        if (!filter.isGenreNameEmpty())
            queryBuilder.likeGenreName(filter.getGenreName());
        return queryBuilder;
    }
}
