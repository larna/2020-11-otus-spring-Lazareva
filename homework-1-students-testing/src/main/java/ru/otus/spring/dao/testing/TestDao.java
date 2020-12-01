package ru.otus.spring.dao.testing;

import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.StudentTest;

import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс доступа к тестам студентов
 */
public interface TestDao {
    /**
     * Поиск теста для студента и за указанную дату
     *
     * @param student - студент
     * @param date    - дата сдачи теста
     * @return будет возвращен найденный тест,
     * @throws TestNotFoundException    если тест не найден
     * @throws IllegalArgumentException если бубут переданы null в качестве аргументов
     */
    StudentTest findByStudentAndDate(Person student, LocalDate date) throws TestNotFoundException, IllegalArgumentException;

    /**
     * Сохранение теста
     *
     * @param test - тест студента
     */
    void save(StudentTest test);

    /**
     * Просмотр всех тестов студентов
     *
     * @return список всех тестов
     */
    List<StudentTest> findAll();

}
