package ru.otus.spring.domain;

import lombok.*;

/**
 * Класс, описывающий жанры
 */
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Genre {
    /**
     * Идентификатор
     */
    private final Long id;
    /**
     * Название жанра
     */
    private final String name;

    public static Genre of(Long genreId) {
        return new Genre(genreId, null);
    }
}
