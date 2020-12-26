package ru.otus.spring.service.testing;

import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.StudentTest;
import ru.otus.spring.domain.testing.results.TestResultsReport;

import java.time.LocalDate;

/**
 * Интерфейс сервиса Тестов
 */
public interface TestService {
    /**
     * Метод создания нового теста
     * @param student объект студента
     * @return - объект теста
     * @throws DuplicateTestException выбрасывает исключение в случае попытки создать тест повторно
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
     * Добавить ответ студента в виде введенного значения к тесту
     *
     * @param test        объекь тест
     * @param studentAnswer ответ студента
     */
    void addAnswerToTest(StudentTest test, Answer studentAnswer);

    /**
     * Получить результаты тест
     *
     * @param test объект тест
     * @return результаты теста в виде строки
     */
    TestResultsReport getTestResults(StudentTest test);
}
