package ru.otus.spring.controller.ui.select;

import org.apache.commons.lang3.StringUtils;
import org.jline.reader.LineReader;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.otus.spring.controller.ui.views.View;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.services.genres.GenreService;

import java.util.List;

/**
 * Компонент для выбора жанра
 */
@Component
public class GenreSelect {
    private static final String INVITE_MESSAGE = "Введите номер жанра";
    private final View<Genre> genreView;
    private LineReader lineReader;
    private final GenreService genreService;

    public GenreSelect(@Lazy LineReader lineReader,
                       View<Genre> genreView,
                       GenreService genreService) {
        this.lineReader = lineReader;
        this.genreView = genreView;
        this.genreService = genreService;
    }

    /**
     * Метод выводит список жанров и ожидает от пользователя выбор в виде номера строки нужного жанра
     *
     * @return объект жанр, полученный из отображенного пользователю списка либо null, если список жанров пуст и
     * выбирать не из чего
     */
    public Genre prompt() {
        List<Genre> genreList = genreService.findAll();
        if (genreList.isEmpty()) {
            System.out.println("Список жанров пуст...");
            return null;
        }
        System.out.println(genreView.getListView(genreList, "Выберите жанр"));
        Genre selectedGenre = null;
        do {
            String number = lineReader.readLine(INVITE_MESSAGE + ": ");
            if (!StringUtils.isEmpty(number)) {
                try {
                    selectedGenre = genreList.get(Integer.parseInt(number) - 1);
                } catch (NumberFormatException e) {
                    System.out.println("Номер должен быть целым числом из таблицы жанров");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Выберите номер из списка жанров");
                }
            }
        } while (selectedGenre == null);
        return selectedGenre;
    }
}

