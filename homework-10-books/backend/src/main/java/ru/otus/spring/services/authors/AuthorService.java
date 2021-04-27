package ru.otus.spring.services.authors;

import ru.otus.spring.domain.Author;

import java.util.List;

/**
 * Сервис для работы с авторами
 */
public interface AuthorService {
    /**
     * Найти всех авторов.
     *
     * @return список авторов
     */
    List<Author> findAll();

    /**
     * Поиск авторов по списку идентификаторов
     * @param authorsIdList
     * @return
     */
    List<Author> findAllByIdIn(List<String> authorsIdList);
}
