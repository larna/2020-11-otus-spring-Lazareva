package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.shell.events.EventsPublisher;
import ru.otus.spring.domain.Person;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.service.ui.login.LoginException;
import ru.otus.spring.service.ui.login.LoginProcessService;

/**
 * Класс обслуживающий консольный интерфейс
 */
@ShellComponent
@RequiredArgsConstructor
public class TestController {
    private static Logger logger = LoggerFactory.getLogger(TestController.class);
    private final EventsPublisher eventsPublisher;
    private final LocalizationService localizationService;
    private final LoginProcessService loginProcessService;
    private volatile Person loginStudent = null;

    @ShellMethod(key = {"l", "login"}, value = "Login student")
    public String login(@ShellOption(help = "Login format: Petrov Peter", defaultValue = "Sidorov Alex")
                                String userName) {
        try {
            Person student = loginProcessService.login(userName);
            loginStudent = student;
            return userName + ", welcome to students testing!";
        } catch (LoginException e) {
            logger.warn("AccessDenied. Student not found.");
            return "AccessDenied. Student not found.";
        }
    }

    @ShellMethod(key = {"t", "test"}, value = "Student testing")
    @ShellMethodAvailability(value = "isTestCommandAvailable")
    public void test() {
        eventsPublisher.publish(loginStudent);
        loginStudent = null;
    }

    private Availability isTestCommandAvailable() {
        return loginStudent == null ? Availability.unavailable("Before login, please") : Availability.available();
    }

    @ShellMethod(key = {"r", "ru"}, value = "Change locale to Russian")
    public String russianLocale() {
        localizationService.selectLocale("ru");
        return "Set russian language";
    }

    @ShellMethod(key = {"e", "en"}, value = "Change locale to English")
    public String englishLocale() {
        localizationService.selectLocale("en");
        return "Set english language";
    }

    @ShellMethod(key = {"d", "de"}, value = "Change locale to German")
    public String germanLocale() {
        localizationService.selectLocale("de");
        return "Set german language";
    }
}
