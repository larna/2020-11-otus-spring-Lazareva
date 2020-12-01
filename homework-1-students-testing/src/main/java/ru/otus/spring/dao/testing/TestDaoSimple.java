package ru.otus.spring.dao.testing;

import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.Question;
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

    @Override
    public StudentTest createNewTest(Person student, List<Question> questions) {
        StudentTest test = new StudentTest(LocalDate.now(), student, questions);
        if (tests.contains(test)) {
            String message = String.format("( student - %s, date - %s )",
                    test.getStudent().getName(),
                    test.getTestDate().format(DateTimeFormatter.ISO_DATE));
            throw new DuplicateTestException(message);
        }

        tests.add(test);
        return test;
    }

    @Override
    public StudentTest findByStudentAndDate(Person student, LocalDate date) {
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

}
