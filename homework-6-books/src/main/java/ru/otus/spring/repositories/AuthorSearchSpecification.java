package ru.otus.spring.repositories;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Author;

import javax.persistence.TypedQuery;

@RequiredArgsConstructor
public class AuthorSearchSpecification {
    private final SearchFilter filter;

    public Boolean isSatisfied() {
        return !filter.isEmpty();
    }

    AuthorQueryBuilder execute() {
        AuthorQueryBuilder authorQueryBuilder = AuthorQueryBuilder.builder();
        if (!filter.isAuthorNameEmpty())
            authorQueryBuilder.likeName(filter.getAuthorName());
        if (!filter.isBookNameEmpty())
            authorQueryBuilder.likeBookName(filter.getBookName());
        if (!filter.isGenreNameEmpty())
            authorQueryBuilder.likeGenreName(filter.getGenreName());
        return authorQueryBuilder;
    }
}
