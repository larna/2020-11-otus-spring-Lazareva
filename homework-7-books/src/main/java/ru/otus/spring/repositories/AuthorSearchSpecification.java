package ru.otus.spring.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Спецификация поиска по фильтру
 */
@RequiredArgsConstructor
public class AuthorSearchSpecification implements Specification<Author> {
    private final SearchFilter filter;

    @Override
    public Predicate toPredicate(Root<Author> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        if (!filter.isBookNameEmpty() || !filter.isGenreNameEmpty()) {
            Join<Author, Book> bookJoin = root.join("books", JoinType.INNER);

            if (!filter.isBookNameEmpty()) {
                predicates.add(criteriaBuilder.like(bookJoin.get("name"), "%" + filter.getBookName() + "%"));
            }
            if (!filter.isGenreNameEmpty()) {
                Join<Book, Genre> genreJoin = bookJoin.join("genre", JoinType.INNER);
                predicates.add(criteriaBuilder.like(genreJoin.get("name"), "%" + filter.getGenreName() + "%"));
            }
        }
        if (!filter.isAuthorNameEmpty())
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.getAuthorName() + "%"));

        Predicate[] predicatesArray = new Predicate[predicates.size()];

        return criteriaBuilder.and(predicates.toArray(predicatesArray));
    }
}
