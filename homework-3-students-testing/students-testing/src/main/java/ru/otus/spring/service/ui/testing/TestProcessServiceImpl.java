package ru.otus.spring.service.ui.testing;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.props.TestProcessProps;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.domain.testing.StudentTest;
import ru.otus.spring.domain.testing.results.TestResultsReport;
import ru.otus.spring.service.io.IOService;
import ru.otus.spring.service.testing.DuplicateTestException;
import ru.otus.spring.service.testing.TestService;
import ru.otus.spring.service.testing.TestServiceImpl;
import ru.otus.spring.service.ui.testing.question.AskQuestionHandler;
import ru.otus.spring.service.ui.testing.question.AskQuestionHandlerNotFoundException;

import java.time.LocalDate;
import java.util.List;

/**
 * Логика связанная с самим процессом тестирования студента
 */
@Service
@AllArgsConstructor
public class TestProcessServiceImpl implements TestProcessService {
    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);
    private final IOService ioService;
    private final TestService testService;
    private final List<AskQuestionHandler> askQuestionServices;
    private final TestProcessProps testProcessProps;

    /**
     * Тестирование студента
     *
     * @param student объект студента
     */
    @Override
    public TestResultsReport testStudent(Person student) {
        StudentTest test;
        try {
            test = testService.createNewTest(student);
            askQuestions(test);

        } catch (DuplicateTestException e) {
            String alreadyExistsMessage = "Test already exists for " + student.getFullName();
            logger.info(alreadyExistsMessage);
            test = testService.getTestByStudentAndDate(student, LocalDate.now());
            String comeBackTomorrowMessage = testProcessProps.getComeBackMessage();
            ioService.outMessage(comeBackTomorrowMessage);
        }
        return testService.getTestResults(test);
    }

    private void askQuestions(StudentTest test) {
        int counter = 1;
        for (Question question : test.getQuestions()) {
            try {
                AskQuestionHandler askQuestionHandler = askQuestionServices.stream()
                        .filter(service -> service.isSuitableQuestion(question))
                        .findFirst()
                        .orElseThrow(AskQuestionHandlerNotFoundException::new);
                Answer answer = askQuestionHandler.askQuestion(counter++, question);
                testService.addAnswerToTest(test, answer);
            } catch (AskQuestionHandlerNotFoundException e) {
                logger.error("Application has wrong configuration. AskQuestionServiceNotFoundException not found for " + question);
                ioService.outMessage(testProcessProps.getErrorMessage());
            }
        }
    }
}
