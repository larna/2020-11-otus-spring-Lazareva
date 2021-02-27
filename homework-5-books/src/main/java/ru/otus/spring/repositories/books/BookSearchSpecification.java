package ru.otus.spring.repositories.books;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Book;

/**
 * Спецификация для поиска книг
 */
@AllArgsConstructor
public class BookSearchSpecification {
    /**
     * фильтр
     */
    private final SearchFilter filter;

    /**
     * Удовлетворяют ли входные параметры спецификации
     * @return true - если фильтр не пуст и false - в противном случае
     */
    public Boolean isSatisfiedBy() {
        return !filter.isEmpty();
    }

    /**
     * Выполнить запросы согласно спецификации
     * @param dao репозиторий
     * @param pageable параметры страницы
     * @return страницу книг
     */
    Page<Book> execute(BookRepository dao, Pageable pageable) {
        if (!filter.isBookNameEmpty() && !filter.isGenreNameEmpty() && !filter.isAuthorNameEmpty())
            return dao.getAllByLikeAuthorNameAndLikeGenreNameAndLikeBookName(filter.getAuthorName(), filter.getGenreName(),
                    filter.getBookName(), pageable);

        if (!filter.isBookNameEmpty() && !filter.isGenreNameEmpty())
            return dao.getAllByLikeGenreNameAndLikeBookName(filter.getGenreName(), filter.getBookName(), pageable);

        if (!filter.isGenreNameEmpty() && !filter.isAuthorNameEmpty())
            return dao.getAllByLikeAuthorNameAndLikeGenreName(filter.getAuthorName(), filter.getGenreName(), pageable);

        if (!filter.isBookNameEmpty() && !filter.isAuthorNameEmpty())
            return dao.getAllByLikeAuthorNameAndLikeBookName(filter.getAuthorName(), filter.getBookName(), pageable);

        if (!filter.isBookNameEmpty())
            return dao.getAllByLikeBookName(filter.getBookName(), pageable);

        if (!filter.isGenreNameEmpty())
            return dao.getAllByLikeGenreName(filter.getGenreName(), pageable);

        if (!filter.isAuthorNameEmpty())
            return dao.getAllByLikeAuthorName(filter.getAuthorName(), pageable);

        return Page.empty();
    }

}
