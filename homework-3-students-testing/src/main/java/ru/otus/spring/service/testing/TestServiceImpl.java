package ru.otus.spring.service.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.props.TestProps;
import ru.otus.spring.dao.testing.TestDao;
import ru.otus.spring.dao.testing.TestNotFoundException;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.domain.testing.StudentTest;
import ru.otus.spring.domain.testing.results.Header;
import ru.otus.spring.domain.testing.results.ReportRow;
import ru.otus.spring.domain.testing.results.TestResultsReport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Реализация сервиса Тестов
 */
@Service
public class TestServiceImpl implements TestService {
    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);
    private final QuestionService questionService;
    private final TestDao dao;
    private final TestProps testProps;

    @Autowired
    public TestServiceImpl(QuestionService questionService,
                           TestDao dao,
                           TestProps testProps) {
        this.questionService = questionService;
        this.dao = dao;
        this.testProps = testProps;
    }

    /**
     * Метод создания нового теста
     *
     * @param student объект студента
     * @return - объект теста
     * @throws DuplicateTestException выбрасывает исключение в случае попытки создать тест повторно
     */
    public StudentTest createNewTest(Person student) throws DuplicateTestException {
        LocalDate testDate = LocalDate.now();
        if (getTestByStudentAndDate(student, testDate) != null)
            throw new DuplicateTestException(String.format("(%s %s)",
                    student.getName(), testDate.format(DateTimeFormatter.ISO_DATE)));

        List<Question> questions = questionService.getQuestions();
        StudentTest test = new StudentTest(testDate, student, questions);
        dao.save(test);
        return test;
    }

    /**
     * Получить тест для указанного студента за указанную дату
     *
     * @param student - объект студента
     * @param date    - дата прохождения теста
     * @return объект тест
     */
    @Override
    public StudentTest getTestByStudentAndDate(Person student, LocalDate date) {
        try {
            return dao.findByStudentAndDate(student, date);
        } catch (TestNotFoundException e) {
            return null;
        }
    }

    /**
     * Добавить новый ответ к тесту
     *
     * @param test          объекь тест
     * @param studentAnswer ответ студента
     */
    @Override
    public void addAnswerToTest(StudentTest test, Answer studentAnswer) {
        if (test.isComplete()) {
            logger.warn("Attempt to add answer to complete test");
            return;
        }
        test.addAnswer(studentAnswer);
        //тут dao ничего не делает, потому что объект в ОП, и предыдущей строкой мы уже все добавили:)
        //но если бы была база то там мы сохранили бы изменения.
        dao.save(test);
    }

    /**
     * Получить результаты тест
     *
     * @param test объект тест
     * @return результаты теста в виде строки
     */
    @Override
    public TestResultsReport getTestResults(StudentTest test) {
        boolean isFailed = test.getFactPercentPassTest() < testProps.getPercent();
        Header header = new Header(test.getStudent(), test.getTestDate());
        TestResultsReport report = new TestResultsReport(isFailed, header);

        IntStream.range(0, test.getQuestions().size()).forEach(i -> {
            Question question = test.getQuestions().get(i);
            Answer studentAnswer = test.getAnswers().get(i);
            ReportRow row = new ReportRow(question, studentAnswer);
            report.addRow(row);
        });
        return report;
    }
}
