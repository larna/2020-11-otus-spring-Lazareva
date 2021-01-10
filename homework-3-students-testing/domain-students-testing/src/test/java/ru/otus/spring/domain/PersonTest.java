package ru.otus.spring.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Person")
class PersonTest {
    @DisplayName("Корректно создает объект")
    @Test
    void shouldHaveCorrectConstructor() {
        Person student = new Person("Ivanov", "Ivan", 18);
        assertAll(() -> assertEquals("Ivan", student.getName()),
                () -> assertEquals("Ivanov", student.getSurname()),
                () -> assertEquals(18, student.getAge()));
    }

    @DisplayName("Корректно формирует полное имя студента из имени и фамилии")
    @Test
    void shouldHaveCorrectFormFullname() {
        assertEquals("Ivan Ivanov", Person.fullNameOf("Ivan", "Ivanov"));
    }
    @DisplayName("Корректно формирует полное имя студента из имени и фамилии")
    @Test
    void shouldHaveCorrectGetFullName() {
        Person student = new Person("Ivanov", "Ivan", 18);
        assertEquals("Ivan Ivanov", student.getFullName());
    }

    @DisplayName("Корректно сравнивает объекты")
    @Test
    void shouldHaveCorrectEquals() {
        Person student = new Person("Ivanov", "Ivan", 18);
        Person sameStudent = new Person("Ivanov", "Ivan", 18);
        Person otherStudent = new Person("Ivanov", "Ivan", 19);
        Person anotherStudent = new Person("Petrov", "Peter", 18);

        assertAll(() -> assertTrue(student.equals(sameStudent)),
                () -> assertFalse(student.equals(otherStudent)),
                () -> assertFalse(student.equals(anotherStudent)));
    }
}