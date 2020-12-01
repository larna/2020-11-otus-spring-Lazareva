package ru.otus.spring.dao.testing;

import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.StudentTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс доступа к тестам студентов
 */
public class TestDaoSimple implements TestDao {
    /**
     * Хранилище тестов
     */
    private final List<StudentTest> tests = new ArrayList<>();

    /**
     * Поиск теста для студента и за указанную дату
     *
     * @param student - студент
     * @param date    - дата сдачи теста
     * @return будет возвращен найденный тест,
     * @throws TestNotFoundException    если тест не найден
     * @throws IllegalArgumentException если бубут переданы null в качестве аргументов
     */
    @Override
    public StudentTest findByStudentAndDate(Person student, LocalDate date) throws TestNotFoundException, IllegalArgumentException {
        if (student == null || date == null)
            throw new IllegalArgumentException("Student or date is null");

        Optional<StudentTest> studentTest = tests.stream()
                .filter(test -> {
                    Boolean isStudentTest = test.getStudent().equals(student);
                    Boolean isSuitableTestDate = test.getTestDate().equals(date);
                    return isStudentTest && isSuitableTestDate;
                }).findFirst();
        if (studentTest.isPresent())
            return studentTest.get();

        String message = String.format("( student - %s, date - %s )",
                student.getName(), date.format(DateTimeFormatter.ISO_DATE));
        throw new TestNotFoundException(message);
    }

    /**
     * Сохранение теста
     * @param test - тест студента
     */
    @Override
    public void save(StudentTest test) {
        if (tests.contains(test)) {//есть уже такой тест   
            return;
        }
        tests.add(test);

    }

    /**
     * Просмотр всех тестов студентов
     * @return список всех тестов
     */
    @Override
    public List<StudentTest> findAll() {
        return tests;
    }

}
