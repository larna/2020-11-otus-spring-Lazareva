package ru.otus.spring.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.results.TestResultsReport;
import ru.otus.spring.service.ui.login.AccessDeniedException;
import ru.otus.spring.service.ui.login.LoginProcessService;
import ru.otus.spring.service.ui.menu.MenuService;
import ru.otus.spring.service.ui.testing.TestProcessService;
import ru.otus.spring.service.ui.testing.TestResultSender;

/**
 * Класс обслуживающий консольный интерфейс
 */
@Controller
public class TestControllerImpl implements TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestControllerImpl.class);
    private static final String ACCESS_DENIED_MESSAGE = "AccessDenied Student not found.";

    private final TestProcessService testProcessService;
    private final LoginProcessService loginService;
    private final TestResultSender resultPrinter;
    private final MenuService menuService;

    @Autowired
    public TestControllerImpl(TestProcessService testProcessService,
                              LoginProcessService loginService,
                              TestResultSender resultPrinter, MenuService menuService) {
        this.testProcessService = testProcessService;
        this.loginService = loginService;
        this.resultPrinter = resultPrinter;
        this.menuService = menuService;
    }

    /**
     * Функция проведения тестирования студентов
     */
    public void start() {
        boolean isContinueTest = true;
        do {
            testStudent();
            menuService.printMenu();
            if (menuService.getCommand() == MenuService.Command.EXIT) {
                isContinueTest = false;
            }
        } while (isContinueTest);
    }

    private void testStudent() {
        try {
            Person student = loginService.login();
            TestResultsReport resultsReport = testProcessService.testStudent(student);
            resultPrinter.send(resultsReport);
        } catch (AccessDeniedException e) {
            logger.info(ACCESS_DENIED_MESSAGE, e);
        } catch (Exception e) {
            logger.error("Student testing error", e);
        }
    }
}
