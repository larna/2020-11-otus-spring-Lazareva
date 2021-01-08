package ru.otus.spring.service.ui.testing;

import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.results.TestResultsReport;

public interface TestProcessService {
    /**
     * Процесс тестирования студента
     * @param student
     */
    TestResultsReport testStudent(Person student);
}
