package ru.otus.spring.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Person")
class PersonTest {
    @DisplayName("Корректно создает объект")
    @Test
    void shouldHaveCorrectConstructor() {
        Person student = new Person("Ivanov", 18);
        assertAll(() -> assertEquals("Ivanov", student.getName()),
                () -> assertEquals(18, student.getAge()));
    }

    @DisplayName("Корректно сравнивает объекты")
    @Test
    void shouldHaveCorrectEquals() {
        Person student = new Person("Ivanov", 18);
        Person sameStudent = new Person("Ivanov", 18);
        Person otherStudent = new Person("Ivanov", 19);
        Person anotherStudent = new Person("Petrov", 18);

        assertAll(() -> assertTrue(student.equals(sameStudent)),
                () -> assertFalse(student.equals(otherStudent)),
                () -> assertFalse(student.equals(anotherStudent)));
    }
}