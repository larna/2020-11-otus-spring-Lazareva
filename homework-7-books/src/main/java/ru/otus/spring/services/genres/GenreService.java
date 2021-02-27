package ru.otus.spring.services.genres;

import ru.otus.spring.domain.Genre;

import java.util.List;

/**
 * Сервис для работы с жанрами
 */
public interface GenreService {
    /**
     * Показать все жанры
     *
     * @return список жанров
     */
    List<Genre> findAll();

    Genre findById(Long id);
}
