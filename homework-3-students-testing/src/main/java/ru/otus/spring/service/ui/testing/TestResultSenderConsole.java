package ru.otus.spring.service.ui.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.results.Header;
import ru.otus.spring.domain.testing.results.ReportRow;
import ru.otus.spring.domain.testing.results.TestResultsReport;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.service.io.IOService;
import ru.otus.spring.service.testing.TestService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Сервис для вывода результатов тестирования
 * Их можно вывести в консоль, отправить по e-mail, в файл в разных форматах...
 */
@Service
public class TestResultSenderConsole implements TestResultSender {
    private static final Logger logger = LoggerFactory.getLogger(TestResultSenderConsole.class);

    private enum Border {TOP, BOTTOM}

    private static final String HEADER_TITLE = "testResult_header";
    private static final String TEST_FAILED = "testResult_failed";
    private static final String TEST_DONE = "testResult_done";

    private static final String TOP = "\n\n****************************************************************";
    private static final String BOTTOM = "****************************************************************\n\n";
    private static final String QUESTION_TEMPLATE = "%d. %s";
    private static final String ANSWER_TEMPLATE = "testResult_answer";
    private static final String RIGHT = "testResult_answerRight";
    private static final String WRONG = "testResult_answerWrong";
    private static final String RIGHT_ANSWERS_TEMPLATE = "testResult_rightAnswersCount";
    private static final String RIGHT_ANSWERS_NOT_DEFINE = "testResult_rightAnswersNotDefined";

    private final IOService ioService;
    private final TestService testService;
    private final LocalizationService localizationService;

    @Autowired
    public TestResultSenderConsole(IOService ioService, TestService testService, LocalizationService localizationService) {
        this.ioService = ioService;
        this.testService = testService;
        this.localizationService = localizationService;
    }

    /**
     * Метод вывода результатов теста
     *
     * @param report объект результаты прохождения теста студента
     */
    @Override
    public void send(TestResultsReport report) {
        if (report == null) {
            logger.warn("Report results is null");
            return;
        }
        printBorder(Border.TOP);
        printHeader(report);
        printContent(report.getRows());
        printBorder(Border.BOTTOM);
    }

    /**
     * Вывод оформления
     *
     * @param border - рамка
     */
    private void printBorder(Border border) {
        String borderOut = "";
        switch (border) {
            case TOP:
                borderOut = TOP;
                break;
            case BOTTOM:
                borderOut = BOTTOM;
                break;
        }
        ioService.outMessage(borderOut);
    }

    /**
     * Вывод шапки результатов
     *
     * @param report - отчет
     */
    private void printHeader(TestResultsReport report) {
        Header header = report.getHeader();

        String student = header.getPerson().getFullName();
        String dateTest = header.getDate().format(DateTimeFormatter.ISO_DATE);
        String title = localizationService.getMessage(HEADER_TITLE, student, dateTest);
        ioService.outMessage(title);

        String resultTestMessage = localizationService.getMessage(report.getIsFailed() ? TEST_FAILED : TEST_DONE);
        ioService.outMessage(resultTestMessage);
        ioService.outMessage("");
    }

    /**
     * Вывод содержимого
     *
     * @param rows - строки
     */
    private void printContent(List<ReportRow> rows) {
        if (rows == null || rows.size() == 0) {
            logger.warn("Content of result report is empty!");
            return;
        }
        IntStream.range(0, rows.size()).forEach(i ->
        {
            ReportRow row = rows.get(i);
            Answer studentAnswer = row.getStudentAnswer();
            ioService.outMessage(String.format(QUESTION_TEMPLATE, i + 1, row.getQuestion().getQuestion()));
            ioService.outMessage(localizationService.getMessage(ANSWER_TEMPLATE, studentAnswer.getAnswer()));
            if (studentAnswer.getIsRightAnswer()) {
                ioService.outMessage(localizationService.getMessage(RIGHT));
            } else {
                ioService.outMessage(localizationService.getMessage(WRONG));
                String rightAnswers = row.getQuestion().getRightAnswers().stream()
                        .map(Answer::getAnswer)
                        .reduce((s1, s2) -> s1 + "," + s2)
                        .orElse(localizationService.getMessage(RIGHT_ANSWERS_NOT_DEFINE));
                ioService.outMessage(localizationService.getMessage(RIGHT_ANSWERS_TEMPLATE, rightAnswers));
            }
        });
    }
}
