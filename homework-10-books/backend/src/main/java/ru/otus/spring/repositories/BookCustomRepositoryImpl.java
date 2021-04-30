package ru.otus.spring.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.spring.controllers.SearchFilter;
import ru.otus.spring.domain.Book;

import java.util.List;

/**
 * Custom Репозиторий книг
 */
public class BookCustomRepositoryImpl implements BookCustomRepository {
    private final MongoTemplate mongoTemplate;

    public BookCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * По-страничный поиск книг согласно фильтру
     * @param filter фильтр
     * @param pageable настройки по-страничного поиска
     * @return страницу книг
     */
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
        long count = mongoTemplate.count(query, Book.class);
        query.with(pageable);
        List<Book> books = mongoTemplate.find(query, Book.class);
        return new PageImpl<>(books, pageable, count);
    }
}
