package ru.otus.spring.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Класс, описывающий жанры
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Genre {
    /**
     * Идентификатор
     */
    @Id
    private String id;
    /**
     * Название жанра
     */
    private String name;
}
