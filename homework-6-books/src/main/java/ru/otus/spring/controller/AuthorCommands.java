package ru.otus.spring.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.controller.dto.AuthorDto;
import ru.otus.spring.domain.Author;
import ru.otus.spring.services.authors.AuthorNotFoundException;
import ru.otus.spring.services.authors.AuthorService;
import ru.otus.spring.services.authors.AuthorServiceException;
import ru.otus.spring.controller.ui.View;

/**
 * Компонент, обслуживающий работу интерфейса с авторами
 */
@ShellComponent
@ShellCommandGroup("author")
public class AuthorCommands {
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

    public AuthorCommands(AuthorService authorService, @Qualifier("authorsView") View<Author> authorsView) {
        this.authorService = authorService;
        this.authorsView = authorsView;
    }

    /**
     * По-страничный вывод авторов
     * @param page страница
     * @param size размер страницы
     * @return
     */
    @ShellMethod(key = {"a", "authors"}, value = "List of all authors")
    public String viewAllAuthors(@ShellOption(value = {"-p", "-page"}, help = "Set index of page", defaultValue = "0") Integer page,
                                 @ShellOption(value = {"-s", "-size"}, help = "Set size of page", defaultValue = DEFAULT_PARAM_PAGE_SIZE) Integer size) {
        if (page < 0 || size < 0)
            return "Страница или размер не монут быть отрицательными!";
        Page<Author> authors = authorService.findAll(PageRequest.of(page, size));
        return showAuthorsPage(authors);
    }

    /**
     * Добавить автора
     * @param name имя автора
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
        try {
            Author author = authorService.save(AuthorDto.toDomain(name, realName, birthday));
            return authorsView.getObjectView(author, "Автор успешно добавлен");
        } catch (AuthorServiceException e) {
            return e.getMessage();
        }
    }

    /**
     * Найти авторов по введенным критериям. Критерии между собой объендиняются по И, поиск производиться с помощью like.
     * @param authorName имя автора
     * @param genreName название жанра
     * @param bookName название книги
     * @param page страница
     * @param size размер страницы
     * @return
     */
    @ShellMethod(key = {"a?", "author-find"}, value = "Find authors")
    public String findAuthors(@ShellOption(value = {"-n", "-name"}, help = "Name of author", defaultValue = "") String authorName,
                              @ShellOption(value = {"-g", "-genre"}, help = "Genre for author", defaultValue = "") String genreName,
                              @ShellOption(value = {"-b", "-book"}, help = "Books of author", defaultValue = "") String bookName,
                              @ShellOption(value = {"-p", "-page"}, help = "Set index of page", defaultValue = "0") Integer page,
                              @ShellOption(value = {"-s", "-size"}, help = "Set size of page", defaultValue = DEFAULT_PARAM_PAGE_SIZE) Integer size) {

        if (page < 0 || size < 0)
            return "Страница или размер не монут быть отрицательными!";

        SearchFilter searchFilter = new SearchFilter(authorName, genreName, bookName);
        Page<Author> authors = authorService.findAllByFilter(searchFilter, PageRequest.of(page, size));
        return showAuthorsPage(authors);
    }

    /**
     * Удалить автора
     * @param id идентиифкатор автора
     * @return
     */
    @ShellMethod(key = {"a-", "author-delete"}, value = "Delete author")
    public String deleteAuthor(@ShellOption(help = "Delete author by id") Long id) {
        if (id == null)
            return "Автор для удаления не выбран";
        try {
            authorService.deleteById(id);
        } catch (AuthorNotFoundException e) {
            return "Автор не найден";
        }catch (Exception e){
            return "Автора не удалось удалить";
        }
        return "Автор успешно удален";
    }

    /**
     * Показать страницу авторов пользователю
     * @param authors страница авторов
     * @return
     */
    private String showAuthorsPage(Page<Author> authors){
        if (authors.isEmpty())
            return "Авторы не найдены";

        String message = String.format("Всего авторов: %d. Показано: %d ", authors.getTotalElements(), authors.getNumberOfElements());
        return authorsView.getListView(authors.getContent(), message);
    }
}
