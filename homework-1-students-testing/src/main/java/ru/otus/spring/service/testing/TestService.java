package ru.otus.spring.service.testing;

import ru.otus.spring.dao.testing.TestNotFoundException;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.StudentTest;

import java.time.LocalDate;

/**
 * Интерфейс сервиса Тестов
 */
public interface TestService {
    /**
     * Создать новый тест
     *
     * @param student объект студента
     * @return новый объект теста
     * @throws DuplicateTestException если тест за указанную дату для этого студента уже существует
     */
    StudentTest createNewTest(Person student) throws DuplicateTestException;

    /**
     * Получить тест для указанного студента за указанную дату
     *
     * @param student - объект студента
     * @param date    - дата прохождения теста
     * @return объект тест
     */
    StudentTest getTestByStudentAndDate(Person student, LocalDate date);

    /**
     * Добавить новый ответ к тесту
     *
     * @param test   объекь тест
     * @param answer ответ студента
     */
    void addAnswerToTest(StudentTest test, Answer answer);

    /**
     * Получить результаты тест
     *
     * @param test объект тест
     * @return результаты теста в виде строки
     */
    String getTestResults(StudentTest test);
}
