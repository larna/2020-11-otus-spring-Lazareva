package ru.otus.spring.service.ui.menu.commands;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.results.TestResultsReport;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.service.ui.login.AccessDeniedException;
import ru.otus.spring.service.ui.login.LoginProcessService;
import ru.otus.spring.service.ui.testing.TestProcessService;
import ru.otus.spring.service.ui.testing.TestResultSender;

@Component
@AllArgsConstructor
public class TestCommandHandler implements CommandHandler {
    private static Logger logger = LoggerFactory.getLogger(TestCommandHandler.class);
    private static final String MENU_COMMAND_TEST_TEXT = "menu_commandTest";

    private final TestProcessService testProcessService;
    private final LoginProcessService loginService;
    private final TestResultSender resultPrinter;
    private final LocalizationService localizationService;

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
        if (localizationService.getMessage(MENU_COMMAND_TEST_TEXT).equals(userChoice))
            return true;
        return false;
    }

    @Override
    public Boolean isContinue() {
        return true;
    }
}
