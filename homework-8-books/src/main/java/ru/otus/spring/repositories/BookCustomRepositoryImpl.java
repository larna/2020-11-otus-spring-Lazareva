package ru.otus.spring.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.ObjectOperators.ObjectToArray.valueOfToArray;

@RequiredArgsConstructor
public class BookCustomRepositoryImpl implements BookCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Book> findAllByFilter(SearchFilter filter, Pageable pageable) {
        Query query = new Query();
        if (!filter.isAuthorNameEmpty()) {
            query.addCriteria(Criteria.where("authors")
                    .elemMatch(Criteria.where("name").regex("\\.*" + filter.getAuthorName() + "\\.*")));
        }
        if (!filter.isBookNameEmpty()) {
            query.addCriteria(Criteria.where("name").regex("\\.*" + filter.getBookName() + "\\.*"));
        }
        if (!filter.isGenreNameEmpty()) {
            query.addCriteria(Criteria.where("genre.name").regex("\\.*" + filter.getGenreName() + "\\.*"));
        }
        Long count = mongoTemplate.count(query, Book.class);
        query.with(pageable);
        List<Book> books = mongoTemplate.find(query, Book.class);
        return new PageImpl<>(books, pageable, count);
    }
}
