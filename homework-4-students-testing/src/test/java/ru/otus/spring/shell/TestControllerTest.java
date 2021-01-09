package ru.otus.spring.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Input;
import org.springframework.shell.Shell;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.shell.events.EventsPublisher;
import ru.otus.spring.domain.Person;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.service.io.IOService;
import ru.otus.spring.service.ui.login.LoginProcessService;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Тест команд shell ")
@SpringBootTest
@ActiveProfiles("test")
@MockBean(IOService.class)
class TestControllerTest {

    @MockBean
    private EventsPublisher eventsPublisher;

    @MockBean
    private LoginProcessService loginProcessService;

    @Autowired
    private LocalizationService localizationService;

    @Autowired
    private Shell shell;

    private static final String COMMAND_LOGIN = "login";
    private static final String COMMAND_LOGIN_SHORT = "l";
    private static final String DEFAULT_LOGIN = "Sidorov Alex";
    private static final String GREETING_PATTERN = "%s, welcome to students testing!";

    private static final String COMMAND_TEST = "test";

    @DisplayName(" должен устанавливать выбранный язык")
    @ParameterizedTest
    @CsvSource(value = {"ru, Set russian language", "en, Set english language", "de, Set german language"})
    void shouldSetSelectedLocale(String localeCommand, String expected) {
        Object res = shell.evaluate(() -> localeCommand);
        assertThat(res).isEqualTo(expected);
        assertEquals(Locale.forLanguageTag(localeCommand), localizationService.getCurrentLocale());
    }

    @DisplayName(" должен приветствовать пользователя, если авторизация прошла успешно")
    @Test
    void shouldReturnExpectedGreetingAfterCorrectLogin() {
        given(loginProcessService.login(eq(DEFAULT_LOGIN))).willReturn(new Person("Sidorov", "Alex", 18));
        given(loginProcessService.login(eq("Petrov Peter"))).willReturn(new Person("Petrov", "Peter", 18));

        Object res = shell.evaluate(() -> COMMAND_LOGIN);
        String expected = String.format(GREETING_PATTERN, DEFAULT_LOGIN);
        assertThat(res).isEqualTo(expected);

        res = shell.evaluate(() -> COMMAND_LOGIN_SHORT);
        expected = String.format(GREETING_PATTERN, DEFAULT_LOGIN);
        assertThat(res).isEqualTo(expected);

        /**
         В случае, если я передаю в метод evaluate лямбду с имитацией моей команды,
         res = shell.evaluate(()->"l 'Petrov Peter'"); //не рабочий вариант
         то она распарсивается в методе Shell.evaluate  неправильно,
         т.к. видимо Shell еще до evaluate понимает, ч
         то здесь будет экранированная строка и применяет другую реализацию интерфейса Input.
         Поэтому здесь подсовываю ему нужную мне реализацию - но это наверное очень криво:)
         */
        Input shellInput = new Input() {
            @Override
            public String rawText() {
                return "l 'Petrov Peter'";
            }

            public List<String> words() {
                return Arrays.asList("l", "Petrov Peter");
            }
        };
        res = shell.evaluate(shellInput);
        expected = String.format(GREETING_PATTERN, "Petrov Peter");
        assertThat(res).isEqualTo(expected);
    }

    @DisplayName(" должен отправить событие для начала тестирования, если пользователь до этого выполнил login")
    @Test
    void shouldSendTestEventForLoginStudent() {
        given(loginProcessService.login(eq(DEFAULT_LOGIN))).willReturn(new Person("Sidorov", "Alex", 18));
        given(loginProcessService.login(eq("Petrov Peter"))).willReturn(new Person("Petrov", "Peter", 18));

        shell.evaluate(() -> COMMAND_LOGIN);
        Person expectedStudent = new Person("Sidorov", "Alex", 18);
        Object res = shell.evaluate(() -> COMMAND_TEST);
        verify(eventsPublisher, times(1))
                .publish(eq(expectedStudent));
    }

    @DisplayName(" возвращать CommandNotCurrentlyAvailable если при попытке выполнения команды test пользователь не выполнил login")
    @Test
    void shouldReturnCommandNotCurrentlyAvailableWhenUserDoesNotLogin() {
        Object res = shell.evaluate(() -> COMMAND_TEST);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }

    @DisplayName(" возвращать CommandNotCurrentlyAvailable если при повторной попытке выполнения команды test без выполнения команды логин непосредственно перед test")
    @Test
    void shouldReturnCommandNotCurrentlyAvailableWhenUserExecuteTestAfterTest() {
        shell.evaluate(() -> COMMAND_LOGIN);
        shell.evaluate(() -> COMMAND_TEST);
        Object res = shell.evaluate(() -> COMMAND_TEST);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }
}