package ru.otus.spring.domain;

import lombok.*;

import java.time.LocalDate;

/**
 * Класс описывающий авторов книг
 */
@EqualsAndHashCode
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Author {
    /**
     * идентификатор
     */
    private final Long id;
    /**
     * Имя автора
     */
    private final String name;
    /**
     * Реальное имя автора, если известно конечно:)
     * По этому имени нет смысла искать, оно может храниться больше для интереса, потому что оно будет явно,
     * менее известно чем псевдоним.
     * И если углубляться в проектирование, то эту информацию лучше вообще вынести в отдельнуй сущность -
     * типа RealAuthorData, потому что и дата рождения и даже пол автора могут быть вымышлены,
     * взять например: Жорж Санд.
     */
    private final String realName;
    /**
     * Дата рождения
     */
    private final LocalDate birthday;

    public static Author of(Long id){
        return new Author(id, null, null, null);
    }
    public static Author of(String name, String realName, LocalDate birthday){
        return new Author(null, name, realName, birthday);
    }
    public static Author of(String name){
        return new Author(null, name, null, null);
    }
}
