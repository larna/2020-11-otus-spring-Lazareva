package ru.otus.spring.service.testing;

import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.StudentTest;

import java.time.LocalDate;

public interface TestService {
    StudentTest createNewTest(Person student);

    StudentTest getTestByStudentAndDate(Person student, LocalDate date);

    void addAnswerToTest(StudentTest test, Answer answer);

    String getTestResults(StudentTest test);
}
