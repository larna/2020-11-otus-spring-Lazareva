package ru.otus.spring.repositories.genre;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Genre;

import java.util.List;

/**
 * Репозиторий для работы с жанрами
 */
public interface GenreRepository {
    /**
     * Показать все жанры
     * @return список жанров
     */
    List<Genre> getAll();
}
