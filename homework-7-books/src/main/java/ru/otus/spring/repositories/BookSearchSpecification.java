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

@RequiredArgsConstructor
public class BookSearchSpecification implements Specification<Book> {
    private final SearchFilter filter;


    @Override
    public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        if (!filter.isBookNameEmpty())
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.getBookName() + "%"));
        if (!filter.isGenreNameEmpty()) {
            Join<Book, Genre> genreJoin = root.join("genre", JoinType.INNER);
            predicates.add(criteriaBuilder.like(genreJoin.get("name"), "%" + filter.getGenreName() + "%"));
        }
        if (!filter.isAuthorNameEmpty()) {
            Join<Book, Author> authorJoin = root.join("authors", JoinType.INNER);
            predicates.add(criteriaBuilder.like(authorJoin.get("name"), "%" + filter.getAuthorName() + "%"));
        }

        Predicate[] predicatesArray = new Predicate[predicates.size()];
        return criteriaBuilder.and(predicates.toArray(predicatesArray));
    }
}
