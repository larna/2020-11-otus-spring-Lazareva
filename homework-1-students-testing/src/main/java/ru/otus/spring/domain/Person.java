package ru.otus.spring.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Класс описывает личные данные человека
 * Я не стала развивать тему студента и то, что класс Person - это будет только его часть,
 * у него внутри могут быть поля характерные для учебного заведения - зачетная книжка, студенческий билет,
 * идентификатор студента...
 * Если посмотреть с другой стороны, то студент будет входить в агрегат Группа/Группы и в зависимости от
 * учебного заведения/курсов иерархию можно будет дополнить.
 * Может еще что-то можно придумать, но на этом моя фантазия иссякла :))))
 */
public class Person {
    /**
     * Имя
     */
    @Getter
    @Setter
    private final String name;
    /**
     * Возраст
     */
    @Getter
    @Setter
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (age != person.age) return false;
        return Objects.equals(name, person.name);
    }

}
