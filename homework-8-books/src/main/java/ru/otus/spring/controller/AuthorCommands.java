package ru.otus.spring.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.controller.ui.select.AuthorSelect;
import ru.otus.spring.controller.ui.views.View;
import ru.otus.spring.domain.Author;
import ru.otus.spring.services.authors.AuthorService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Компонент, обслуживающий работу интерфейса с авторами
 */
@ShellComponent
@ShellCommandGroup("author")
public class AuthorCommands {
    /**
     * формат даты
     */
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    /**
     * Значения размера страницы по-умолчанию
     */
    private static final String DEFAULT_PARAM_PAGE_SIZE = "10";
    /**
     * Сервис для работы с авторами
     */
    private final AuthorService authorService;
    /**
     * Отображение авторов пользователю
     */
    private final View<Author> authorsView;
    private final AuthorSelect authorSelect;

    public AuthorCommands(AuthorService authorService,
                          @Qualifier("authorsView") View<Author> authorsView,
                          AuthorSelect authorSelect) {
        this.authorService = authorService;
        this.authorsView = authorsView;
        this.authorSelect = authorSelect;
    }

    /**
     * По-страничный вывод авторов
     *
     * @param page страница
     * @param size размер страницы
     * @return
     */
    @ShellMethod(key = {"a", "authors"}, value = "List of all authors")
    public String viewAllAuthors(@ShellOption(value = {"-p", "-page"}, help = "Set index of page", defaultValue = "0") Integer page,
                                 @ShellOption(value = {"-s", "-size"}, help = "Set size of page", defaultValue = DEFAULT_PARAM_PAGE_SIZE) Integer size) {
        if (page < 0 || size < 0)
            return "Страница или размер не монут быть отрицательными!";
        List<Author> authors = authorService.findAll();
        return showAuthorsPage(authors);
    }

    /**
     * Добавить автора
     *
     * @param name     имя автора
     * @param realName настоящее имя автора
     * @param birthday день рождения
     * @return
     */
    @ShellMethod(key = {"a+", "author-create"}, value = "New author")
    public String newAuthor(@ShellOption(value = {"-name"}, help = "Name or alias of author") String name,
                            @ShellOption(value = {"-real-name"}, help = "Real name of author", defaultValue = "") String realName,
                            @ShellOption(value = {"-birthday"}, help = "Birthday in format: dd.MM.yyyy", defaultValue = "") String birthday) {

        if (name == null || name.isEmpty())
            return "Добавление невозможно. Имя автора не должно быть пустым!";
        Author author = Author.builder()
                .name(name)
                .realName(realName)
                .birthday(birthdayStringToDate(birthday))
                .build();
        authorService.save(author);
        return authorsView.getObjectView(author, "Автор успешно добавлен");
    }

    /**
     * Показать страницу авторов пользователю
     *
     * @param authors страница авторов
     * @return
     */
    private String showAuthorsPage(List<Author> authors) {
        if (authors.isEmpty())
            return "Авторы не найдены";

        String message = String.format("Всего авторов: %d", authors.size());
        return authorsView.getListView(authors, message);
    }

    private static LocalDate birthdayStringToDate(String birthday) {
        if (birthday == null || birthday.isEmpty())
            return null;
        try {
            return LocalDate.parse(birthday, DateTimeFormatter.ofPattern(DATE_FORMAT));
        } catch (DateTimeParseException e) {
            throw new BirthdayFormatException();
        }
    }
}
