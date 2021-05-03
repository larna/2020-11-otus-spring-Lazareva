package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
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
    @Id
    private String id;
    private String name;
    /**
     * International Standard Book Number
     */
    private String isbn;
    private Genre genre;
    private List<Author> authors;
}
