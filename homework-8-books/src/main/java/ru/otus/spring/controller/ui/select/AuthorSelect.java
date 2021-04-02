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

@Component
public class AuthorSelect {
    private static final String INVITE_MESSAGE = "Введите номера авторов через запятую";
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

    public List<Author> prompt() {
        List<Author> authors = authorService.findAll();
        System.out.println(authorView.getListView(authors, "Выберите авторов"));
        List<Author> selectedAuthors = null;
        do {
            String authorsId = lineReader.readLine(INVITE_MESSAGE + ": ");
            if (!StringUtils.isEmpty(authorsId)) {
                try {
                    selectedAuthors = getAuthorsFromParameter(authorsId, authors);
                } catch (NumberFormatException| IndexOutOfBoundsException e) {
                    System.out.println("Номер должен быть целым числом из таблицы жанров");
                }
            }
        } while (selectedAuthors == null);
        return selectedAuthors;
    }

    private List<Author> getAuthorsFromParameter(String authorsIdString, List<Author> authors) {
        return Arrays.stream(authorsIdString.split(","))
                .map(Integer::parseInt)
                .map(idx -> authors.get(idx - 1))
                .collect(Collectors.toList());
    }
}

