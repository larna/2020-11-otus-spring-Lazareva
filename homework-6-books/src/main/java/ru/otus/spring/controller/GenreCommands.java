package ru.otus.spring.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.controller.ui.View;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.services.genres.GenreService;

import java.util.List;

/**
 * Компонент, по сути контроллер, обслуживающий работу интерфейса с жанрами
 */
@ShellComponent
@ShellCommandGroup("genre")
public class GenreCommands {
    /**
     * Сервис для работы с жанрами
     */
    private final GenreService genreService;
    /**
     * Представление жанров для пользователя
     */
    private final View<Genre> genreView;

    public GenreCommands(GenreService genreService, @Qualifier("genresView") View<Genre> genreView) {
        this.genreService = genreService;
        this.genreView = genreView;
    }

    /**
     * Показать все жанры
     *
     * @return таблицу авторов
     */
    @ShellMethod(key = {"g", "genres"}, value = "List of all genres")
    public String viewAllAuthors() {
        List<Genre> genres = genreService.findAll();

        if (genres.isEmpty())
            return "Жанры не найдены";

        String message = String.format("Всего жанров: %d", genres.size());
        return genreView.getListView(genres, message);
    }
}
