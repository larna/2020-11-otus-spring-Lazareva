package ru.otus.spring.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для описания книги
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
@NamedEntityGraph(name = "books-genre-authors-entity-graph",
        attributeNodes = {@NamedAttributeNode("genre"), @NamedAttributeNode("authors")})
public class Book {
    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * Название книги
     */
    @Column(name = "name", nullable = false)
    private String name;
    /**
     * International Standard Book Number
     */
    @Column(name = "isbn", unique = true)
    private String isbn;
    /**
     * Жанр
     */
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "genre_id", referencedColumnName = "id")
    private Genre genre;
    /**
     * Авторы
     */
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"))
    private List<Author> authors;

    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.DETACH})
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Comment> comments;
}
