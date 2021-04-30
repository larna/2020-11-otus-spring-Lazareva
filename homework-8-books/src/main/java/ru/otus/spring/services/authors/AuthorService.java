package ru.otus.spring.services.authors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Author;

import java.util.List;

/**
 * Сервис для работы с авторами
 */
public interface AuthorService {
    /**
     * Сохранить автора
     *
     * @param author объект автор
     * @return сохраненный автор
     */
    Author save(Author author);

    /**
     * Найти всех авторов.
     *
     * @return список авторов
     */
    List<Author> findAll();
}
