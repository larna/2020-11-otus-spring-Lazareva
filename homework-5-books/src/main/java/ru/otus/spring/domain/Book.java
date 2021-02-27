package ru.otus.spring.domain;

import lombok.*;

import java.util.List;
import java.util.Objects;

/**
 * Класс для описания книги
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Book {
    /**
     * Идентификатор
     */
    private final Long id;
    /**
     * Название книги
     */
    private final @NonNull String name;
    /**
     * International Standard Book Number
     */
    private final String isbn;
    /**
     * Жанр
     */
    private final @NonNull Genre genre;
    /**
     * Авторы
     */
    private final @NonNull List<Author> authors;
}
