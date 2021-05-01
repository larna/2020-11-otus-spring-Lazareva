package ru.otus.spring.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Класс, описывающий жанры
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "genres")
public class Genre {
    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * Название жанра
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    /**
     * Книги жанра
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "genre", cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Book> books;

}
