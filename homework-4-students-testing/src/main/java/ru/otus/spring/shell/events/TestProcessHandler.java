package ru.otus.spring.shell.events;

import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.testing.results.TestResultsReport;
import ru.otus.spring.service.ui.testing.TestProcessService;
import ru.otus.spring.service.ui.testing.TestResultSender;

/**
 * Обработчик события тестирования студента
 */
@Component
public class TestProcessHandler {

    private final TestProcessService testProcessService;
    private final TestResultSender resultPrinter;

    public TestProcessHandler(TestProcessService testProcessService, TestResultSender resultPrinter) {
        this.testProcessService = testProcessService;
        this.resultPrinter = resultPrinter;
    }

    @SneakyThrows
    @EventListener
    public void onApplicationEvent(TestEvent loginEvent) {
        Thread.sleep(100);
        TestResultsReport resultsReport = testProcessService.testStudent(loginEvent.getStudent());
        resultPrinter.send(resultsReport);
    }
}
