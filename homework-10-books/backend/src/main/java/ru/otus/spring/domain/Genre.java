package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Класс, описывающий жанры
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Genre {
    @Id
    private String id;
    private String name;
}
