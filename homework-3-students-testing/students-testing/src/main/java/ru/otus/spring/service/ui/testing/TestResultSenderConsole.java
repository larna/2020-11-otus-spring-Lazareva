package ru.otus.spring.service.ui.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.props.TestResultProps;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.results.Header;
import ru.otus.spring.domain.testing.results.ReportRow;
import ru.otus.spring.domain.testing.results.TestResultsReport;
import ru.otus.spring.service.io.IOService;

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

    private static final String TOP = "\n\n****************************************************************";
    private static final String BOTTOM = "****************************************************************\n\n";
    private static final String QUESTION_TEMPLATE = "%d. %s";

    private final IOService ioService;
    private final TestResultProps testResultProps;

    @Autowired
    public TestResultSenderConsole(IOService ioService, TestResultProps testResultProps) {
        this.ioService = ioService;
        this.testResultProps = testResultProps;
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
        String title = testResultProps.getHeaderMessage(header);
        ioService.outMessage(title);

        String resultTestMessage = testResultProps.getStatusTestMessage(report.getIsFailed());
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
            ioService.outMessage(testResultProps.getAnswerMessage(studentAnswer));

            if (!studentAnswer.getIsRightAnswer()) {
                String rightAnswers = row.getQuestion().getRightAnswers().stream()
                        .map(Answer::getAnswer)
                        .reduce((s1, s2) -> s1 + "," + s2)
                        .orElse(testResultProps.getNotDefinedRightAnswersMessage());
                ioService.outMessage(testResultProps.getRightAnswersMessage(rightAnswers));
            }
        });
    }
}
