package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Класс описывающий авторов книг
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Author {
    @Id
    private String id;
    private String name;
    /**
     * Реальное имя автора, если известно конечно:)
     * По этому имени нет смысла искать, оно может храниться больше для интереса, потому что оно будет явно,
     * менее известно чем псевдоним.
     * И если углубляться в проектирование, то эту информацию лучше вообще вынести в отдельную сущность -
     * типа RealAuthorData, потому что и дата рождения и даже пол автора могут быть вымышлены,
     * взять например: Жорж Санд.
     */
    private String realName;
    private LocalDate birthday;
}
