package ru.otus.spring.dao.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.StudentTest;
import ru.otus.spring.util.testing.QuestionFormatException;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс TestDaoSimple")
class TestDaoSimpleStudentTest {

    private TestDao dao;

    @BeforeEach
    void setUp() {
        dao = new TestDaoSimple();
        Person student = new Person("Ivanov", 19);
        StudentTest test = new StudentTest(LocalDate.now(), student, new ArrayList<>());
        dao.save(test);

    }

    @DisplayName("Корректно создает новый тест для студента")
    @Test
    void shouldHaveCorrectSave() {
        Person student = new Person("Ivanov", 19);
        StudentTest test = new StudentTest(LocalDate.now(), student, new ArrayList<>());

        assertThat(dao.findAll())
                .hasSize(1)
                .contains(test);
    }

    @DisplayName("Показывает всех студентов")
    @Test
    void shouldHaveCorrectFindAll() {
        //Ivanov уже сохранен
        Person petrov = new Person("Petrov", 19);
        StudentTest petrovTest = new StudentTest(LocalDate.now(), petrov, new ArrayList<>());
        dao.save(petrovTest);

        Person ivanov = new Person("Ivanov", 19);
        StudentTest ivanovTest = dao.findByStudentAndDate(ivanov, LocalDate.now());

        assertThat(dao.findAll())
                .hasSize(2)
                .contains(petrovTest, ivanovTest);
    }

    @DisplayName("Не сохраняет дубликаты записей")
    @Test
    void shouldHaveCorrectSaveWithoutDuplicate() {
        Person student = new Person("Ivanov", 19);
        StudentTest test = new StudentTest(LocalDate.now(), student, new ArrayList<>());
        dao.save(test);
        assertThat(dao.findAll())
                .hasSize(1)
                .contains(test);
    }

    @DisplayName("Корректно находит тест студента")
    @Test
    void findByStudentAndDate() {
        Person searchStudent = new Person("Ivanov", 19);
        StudentTest test = dao.findByStudentAndDate(searchStudent, LocalDate.now());
        assertAll(() -> assertNotNull(test),
                () -> assertEquals("Ivanov", test.getStudent().getName()),
                () -> assertEquals(19, test.getStudent().getAge()),
                () -> assertEquals(LocalDate.now(), test.getTestDate()));
    }

    @DisplayName("Выбрасывает исключение если не находит")
    @Test
    void shouldHaveTextNotFoundExceptionIfTestNoFound() {
        Person searchStudent = new Person("Petrov", 19);
        Exception exception = assertThrows(TestNotFoundException.class,
                () -> dao.findByStudentAndDate(searchStudent, LocalDate.now()));
        assertTrue(exception.getMessage().contains("Test not found"));
    }

}