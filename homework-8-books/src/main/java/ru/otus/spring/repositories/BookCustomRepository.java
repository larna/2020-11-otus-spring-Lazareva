package ru.otus.spring.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Book;

public interface BookCustomRepository {
    /**
     * найти все книги согласно фильтру
     * @param filter
     * @param pageable
     * @return
     */
    Page<Book> findAllByFilter(SearchFilter filter, Pageable pageable);
}
