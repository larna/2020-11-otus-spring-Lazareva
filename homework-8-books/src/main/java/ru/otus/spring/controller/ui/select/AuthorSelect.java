package ru.otus.spring.controller.ui.select;

import org.apache.commons.lang3.StringUtils;
import org.jline.reader.LineReader;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.otus.spring.controller.ui.views.View;
import ru.otus.spring.domain.Author;
import ru.otus.spring.services.authors.AuthorService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Компонент для выбора авторов
 */
@Component
public class AuthorSelect {
    private static final String INVITE_MESSAGE = "Введите номера авторов через запятую";
    private static final String SELECT_AUTHORS = "Выберите авторов";
    private final View<Author> authorView;
    private LineReader lineReader;
    private final AuthorService authorService;

    public AuthorSelect(@Lazy LineReader lineReader,
                        View<Author> authorView,
                        AuthorService authorService) {
        this.lineReader = lineReader;
        this.authorView = authorView;
        this.authorService = authorService;
    }

    /**
     * Метод отображающий список авторов и обрабатывающий выбор пользователя
     *
     * @return список выбранных пользователем авторов
     */
    public List<Author> prompt() {
        List<Author> authors = authorService.findAll();
        System.out.println(authorView.getListView(authors, SELECT_AUTHORS));
        List<Author> selectedAuthors = null;
        do {
            String authorsId = lineReader.readLine(INVITE_MESSAGE + ": ");
            if (!StringUtils.isEmpty(authorsId)) {
                try {
                    selectedAuthors = mapUserChoiceToAuthors(authorsId, authors);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("Номер должен быть целым числом из таблицы авторов");
                }
            }
        } while (selectedAuthors == null);
        return selectedAuthors;
    }

    /**
     * Отображение введенных номеров строк авторов в список авторов
     * @param authorsIdString строка с выбранными пользователем номерами авторов
     * @param authors список авторов, отоборажаемых пользователю на экран
     * @return список выбранных авторов
     */
    private List<Author> mapUserChoiceToAuthors(String authorsIdString, List<Author> authors) {
        return Arrays.stream(authorsIdString.split(","))
                .map(Integer::parseInt)
                .map(idx -> authors.get(idx - 1))
                .collect(Collectors.toList());
    }
}

