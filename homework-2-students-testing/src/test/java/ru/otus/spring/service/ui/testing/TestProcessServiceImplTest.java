package ru.otus.spring.service.ui.testing;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.results.TestResultsReport;
import ru.otus.spring.service.io.IOService;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@DisplayName("Класс TestProcessService")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestProcessContextConfig.class})
class TestProcessServiceImplTest {
    @Autowired
    private IOService ioService;
    @Autowired
    private TestProcessService testProcessService;

    private static Person student = new Person("Ivanov", "Ivan", 18);
    private static final Integer expectedResultRows = 5;
    private Condition<TestResultsReport> testDone = new Condition<TestResultsReport>((report) -> !report.getIsFailed(), "Тест пройден");

    private Condition<TestResultsReport> hasFiveRows = new Condition<TestResultsReport>((report) -> report.getRows().size() == expectedResultRows,
            "Результат содержит 5 строк");

    @BeforeEach
    void setUp() {
        Mockito.reset(ioService);
    }

    @DisplayName("Корректно проводит тестирование студента и возвращает результат тестирования")
    @Test
    void shouldCorrectTestStudent() {
        Predicate<TestResultsReport> allAnswerIsRight = (report) -> report.getRows().stream()
                .filter(row -> row.getStudentAnswer().getIsRightAnswer()).count() == expectedResultRows;
        Condition<TestResultsReport> rightAnswerInTest = new Condition<TestResultsReport>(allAnswerIsRight, "Все ответы правильные");

        TestResultsReport actualResultsReport = testProcessService.testStudent(student);
        assertThat(actualResultsReport)
                .isNotNull()
                .has(testDone)
                .has(hasFiveRows)
                .has(rightAnswerInTest)
                .extracting("header.person").isEqualTo(student);
    }

    @DisplayName("Возвращает результаты если тест в этот день уже пройден")
    @Test
    void shouldHaveReturnResultIfTestAlreadyExists() {
        Predicate<TestResultsReport> allAnswerIsRight = (report) -> report.getRows().stream()
                .filter(row -> row.getStudentAnswer().getIsRightAnswer()).count() == expectedResultRows;
        Condition<TestResultsReport> rightAnswerInTest = new Condition<TestResultsReport>(allAnswerIsRight, "Все ответы правильные");

        testProcessService.testStudent(student);

        TestResultsReport actualResultsReport = testProcessService.testStudent(student);
        assertThat(actualResultsReport)
                .isNotNull()
                .has(testDone)
                .has(hasFiveRows)
                .has(rightAnswerInTest)
                .extracting("header.person").isEqualTo(student);
        verify(ioService, times(1)).outMessage(contains("Pardon"));
    }
}