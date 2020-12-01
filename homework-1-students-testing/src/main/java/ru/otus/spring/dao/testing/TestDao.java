package ru.otus.spring.dao.testing;

import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.domain.testing.StudentTest;

import java.time.LocalDate;
import java.util.List;

public interface TestDao {
    StudentTest createNewTest(Person student, List<Question> questions) throws DuplicateTestException;

    StudentTest findByStudentAndDate(Person student, LocalDate date) throws TestNotFoundException;
}
