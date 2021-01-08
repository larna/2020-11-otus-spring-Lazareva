package ru.otus.spring.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Класс описывает личные данные человека
 * Я не стала развивать тему студента и то, что класс Person - это будет только его часть,
 * у него внутри могут быть поля характерные для учебного заведения - зачетная книжка, студенческий билет,
 * идентификатор студента...
 * Если посмотреть с другой стороны, то студент будет входить в агрегат Группа/Группы и в зависимости от
 * учебного заведения/курсов иерархию можно будет дополнить.
 * Может еще что-то можно придумать, но на этом моя фантазия иссякла :))))
 */
@EqualsAndHashCode
@Getter
@NonNull
public class Person {
    /**
     * Фамилия
     */
    private final String surname;
    /**
     * Имя
     */
    private final String name;
    /**
     * Возраст
     */
    private final int age;

    public Person(@NonNull String surname, @NonNull String name, @NonNull int age) {
        this.surname = surname;
        this.name = name;
        this.age = age;
    }

    public String getFullName() {
        return Person.fullNameOf(name, surname);
    }

    public static String fullNameOf(String name, String surname) {
        return name + " " + surname;
    }
}
