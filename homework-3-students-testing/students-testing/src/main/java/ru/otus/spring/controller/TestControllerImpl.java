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
    private final MenuService menuService;

    @Autowired
    public TestControllerImpl(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * Функция проведения тестирования студентов
     */
    public void start() {
        Boolean isContinue;
        do {
            menuService.showMenu();
            isContinue = menuService.handleUserChoice();
        } while (isContinue);
    }
}
