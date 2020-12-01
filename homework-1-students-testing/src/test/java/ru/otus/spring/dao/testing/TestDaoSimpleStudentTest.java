package ru.otus.spring.dao.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.StudentTest;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс TestDaoSimple")
class TestDaoSimpleStudentTest {

    private TestDao dao;

    @BeforeEach
    void setUp() {
        dao = new TestDaoSimple();
    }

    @DisplayName("Корректно создает новый тест для студента")
    @Test
    void createNewTest() {
        Person student = new Person("Ivanov", 19);
        StudentTest test = dao.createNewTest(student, new ArrayList<>());
        assertAll(() -> assertNotNull(test, "Тест должен быть создан"),
                () -> assertEquals("Ivanov", test.getStudent().getName()),
                () -> assertEquals(19, test.getStudent().getAge()),
                () -> assertEquals(LocalDate.now(), test.getTestDate()));
    }

    @DisplayName("Выбрасывает исключение при создании нового теста для студента, если тест за такой день и для этого студента уже есть")
    @Test
    void shouldHaveDuplicateTestExceptionInCreateNewTest() {
        Person student = new Person("Ivanov", 19);
        StudentTest test = dao.createNewTest(student, new ArrayList<>());
        Exception exception = assertThrows(DuplicateTestException.class,
                () -> dao.createNewTest(student, new ArrayList<>()));
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @DisplayName("Корректно находит тест студента")
    @Test
    void findByStudentAndDate() {
        Person student = new Person("Ivanov", 19);
        dao.createNewTest(student, new ArrayList<>());

        Person searchStudent = new Person("Ivanov", 19);
        StudentTest test = dao.findByStudentAndDate(student, LocalDate.now());
        assertAll(() -> assertNotNull(test),
                () -> assertEquals("Ivanov", test.getStudent().getName()),
                () -> assertEquals(19, test.getStudent().getAge()),
                () -> assertEquals(LocalDate.now(), test.getTestDate()));
    }
}