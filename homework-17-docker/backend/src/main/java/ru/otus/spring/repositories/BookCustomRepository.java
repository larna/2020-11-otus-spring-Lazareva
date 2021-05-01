package ru.otus.spring.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.controllers.SearchFilter;
import ru.otus.spring.domain.Book;

/**
 * Кастомный репозиторий книг
 */
public interface BookCustomRepository {
    /**
     * найти все книги согласно фильтру
     * @param filter фильтр
     * @param pageable настройки по-страничного поиска
     * @return страницу книг
     */
    Page<Book> findAllByFilter(SearchFilter filter, Pageable pageable);
}
