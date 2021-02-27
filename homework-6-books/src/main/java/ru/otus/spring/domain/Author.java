package ru.otus.spring.domain;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс описывающий авторов книг
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "authors")
public class Author {
    /**
     * идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Exclude
    private Long id;
    /**
     * Имя автора
     */
    @Column(name = "name", nullable = false)
    private String name;
    /**
     * Реальное имя автора, если известно конечно:)
     * По этому имени нет смысла искать, оно может храниться больше для интереса, потому что оно будет явно,
     * менее известно чем псевдоним.
     * И если углубляться в проектирование, то эту информацию лучше вообще вынести в отдельнуй сущность -
     * типа RealAuthorData, потому что и дата рождения и даже пол автора могут быть вымышлены,
     * взять например: Жорж Санд.
     */
    @Column(name = "real_name")
    private String realName;
    /**
     * Дата рождения
     */
    @Column(name = "birthday")
    private LocalDate birthday;
    /**
     * Книги автора
     */
    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @EqualsAndHashCode.Exclude
    private List<Book> books;
}
