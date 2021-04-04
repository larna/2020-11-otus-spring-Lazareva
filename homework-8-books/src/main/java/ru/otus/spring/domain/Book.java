package ru.otus.spring.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Класс для описания книги
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Book {
    /**
     * Идентификатор
     */
    @Id
    private String id;
    /**
     * Название книги
     */
    private String name;
    /**
     * International Standard Book Number
     */
    private String isbn;
    /**
     * Жанр
     */
    private Genre genre;
    /**
     * Авторы
     */
    private List<Author> authors;
}
