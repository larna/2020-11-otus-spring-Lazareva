package ru.otus.spring.services.books;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Book;

/**
 * Сервис для работы с книгами
 */
public interface BookService {
    /**
     * Сохранить книгу
     *
     * @param book объект книга
     * @return сохраненный объект книги
     */
    Book save(Book book);

    /**
     * Удалить книгу
     *
     * @param book книга
     */
    void delete(Book book);

    /**
     * По-страничный поиск всех книг
     *
     * @param pageable параметры по-страничного поиска
     * @return страница найденных книг
     */
    Page<Book> findAll(Pageable pageable);

    /**
     * По-страничный поиск книг по фильтру
     *
     * @param filter   фильр
     * @param pageable параметры по-страничного поиска
     * @return страница найденных книг
     */
    Page<Book> findAllByFilter(SearchFilter filter, Pageable pageable);
}
