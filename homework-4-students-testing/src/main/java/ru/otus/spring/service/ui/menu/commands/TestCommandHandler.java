package ru.otus.spring.service.ui.menu.commands;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.results.TestResultsReport;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.service.ui.login.AccessDeniedException;
import ru.otus.spring.service.ui.login.LoginProcessService;
import ru.otus.spring.service.ui.testing.TestProcessService;
import ru.otus.spring.service.ui.testing.TestResultSender;

@Component
public class TestCommandHandler implements CommandHandler {
    private static Logger logger = LoggerFactory.getLogger(TestCommandHandler.class);

    private final TestProcessService testProcessService;
    private final LoginProcessService loginService;
    private final TestResultSender resultPrinter;
    private final @NonNull String testCommand;

    public TestCommandHandler(TestProcessService testProcessService,
                              LoginProcessService loginService,
                              TestResultSender resultPrinter,
                              @Value("${application.menu.commands.test}") String testCommand) {
        this.testProcessService = testProcessService;
        this.loginService = loginService;
        this.resultPrinter = resultPrinter;
        this.testCommand = testCommand;
    }

    @Override
    public void handle(String userChoice) {
        try {
            Person student = loginService.login();
            TestResultsReport resultsReport = testProcessService.testStudent(student);
            resultPrinter.send(resultsReport);
        } catch (AccessDeniedException e) {
            logger.info("AccessDenied Student not found.", e);
        } catch (Exception e) {
            logger.error("Student testing error", e);
        }
    }

    @Override
    public Boolean isSuitableCommand(String userChoice) {
        if (testCommand.equals(userChoice))
            return true;
        return false;
    }

    @Override
    public Boolean isContinue() {
        return true;
    }
}
