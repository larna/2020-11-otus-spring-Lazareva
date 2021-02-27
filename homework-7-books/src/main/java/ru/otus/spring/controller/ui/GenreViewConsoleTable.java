package ru.otus.spring.controller.ui;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Genre;

import java.util.List;

/**
 * Представление жанров
 */
@Component("genresView")
@RequiredArgsConstructor
public class GenreViewConsoleTable implements View<Genre> {
    private static final String[] TABLE_HEADER_ROW = new String[]{"ID", "Название жанра"};

    /**
     * Получить отобрадение списка жанров в виде таблицы
     *
     * @param genres  список жанров
     * @param message сообщение
     * @return отображение списка жанров
     */
    @Override
    public String getListView(List<Genre> genres, String message) {
        if (genres == null || genres.size() == 0)
            return "Жанр не найден...";

        String result = getGenresTable(genres).render();
        return String.format("%s\n %s\n", result, message);
    }

    /**
     * Получить отображение одного жанра
     *
     * @param genre   объект жанр
     * @param message сообщение пользователю
     * @return отображение жанра
     */
    @Override
    public String getObjectView(Genre genre, String message) {
        if (genre == null)
            throw new IllegalArgumentException("Для отображения жанра передали нулевой объект");
        return getListView(List.of(genre), message);
    }

    /**
     * Получить таблицы для списка жанров
     *
     * @param genres жанры
     * @return таблица
     */
    private AsciiTable getGenresTable(List<Genre> genres) {
        final AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow(TABLE_HEADER_ROW);
        table.addRule();
        genres.stream().forEach(genre -> {
            table.addRow(genreToRow(genre));
            table.addRule();
        });
        table.getRenderer().setCWC(new CWC_LongestWordMin(new int[]{-1, 30}));
        return table;

    }

    /**
     * Получить строку таблицы
     *
     * @param genre объект жанр
     * @return массив полей для отображения в строке таблицы
     */
    private String[] genreToRow(Genre genre) {
        String idCell = genre.getId().toString();
        String name = genre.getName();
        return new String[]{idCell, name};
    }
}
