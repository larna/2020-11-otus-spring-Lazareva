package ru.otus.spring.service.ui.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
public class TestProcessServiceImpl implements TestProcessService {
    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);

    @Value("${test.process.messages.comeback}")
    private final String COME_BACK_TOMORROW_MESSAGE = "Pardon, you already pass the test today. Come back tomorrow.";
    private static final String TEST_ALREADY_EXISTS_MESSAGE = "Test already exists.";
    private static final String ERROR_MESSAGE = "Was found errors in application. Contact support.";

    private final IOService ioService;
    private final TestService testService;
    private final List<AskQuestionHandler> askQuestionServices;

    @Autowired
    public TestProcessServiceImpl(IOService ioService,
                                  TestService testService,
                                  List<AskQuestionHandler> askQuestionHandlers) {
        this.ioService = ioService;
        this.testService = testService;
        this.askQuestionServices = askQuestionHandlers;
    }

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
            logger.info(TEST_ALREADY_EXISTS_MESSAGE);
            test = testService.getTestByStudentAndDate(student, LocalDate.now());
            ioService.outMessage(COME_BACK_TOMORROW_MESSAGE);
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
                ioService.outMessage(ERROR_MESSAGE);
            }
        }
    }
}
