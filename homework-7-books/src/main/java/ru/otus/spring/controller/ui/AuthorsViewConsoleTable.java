package ru.otus.spring.controller.ui;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Author;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Представление авторов для пользователя
 */
@Component("authorsView")
@RequiredArgsConstructor
public class AuthorsViewConsoleTable implements View<Author> {
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final String DEFAULT_EMPTY_VALUE = "-";
    private static final String[] TABLE_HEADER_ROW = new String[]{"ID", "Имя", "Настоящее имя, если автор издается под псевдонимом", "Дата рождения"};

    /**
     * Отображение списка авторов
     *
     * @param authors авторы
     * @param message сообщение
     * @return
     */
    @Override
    public String getListView(List<Author> authors, String message) {
        if (authors == null || authors.size() == 0)
            return "Авторы не найдены...";

        String result = getAuthorsTable(authors).render();
        return String.format("%s\n %s\n", result, message);
    }

    /**
     * Отображение объекта в виде таблицы с заголовком и содержащим единственную строку с информацией из переданного объекта
     *
     * @param author  объект автора для отображения
     * @param message сообщение пользователю
     * @return
     */
    @Override
    public String getObjectView(Author author, String message) {
        if (author == null)
            throw new IllegalArgumentException("Для отображения автора передали нулевой объект");
        return getListView(List.of(author), message);
    }

    /**
     * Создание и настройка таблицы
     *
     * @param authors список авторов
     * @return
     */
    private AsciiTable getAuthorsTable(List<Author> authors) {
        final AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow(TABLE_HEADER_ROW);
        table.addRule();
        authors.stream().forEach(author -> {
            table.addRow(authorToRow(author));
            table.addRule();
        });
        table.getRenderer().setCWC(new CWC_LongestWordMin(new int[]{-1, 30, 30, 15}));
        return table;

    }

    /**
     * Отображение автора в строку таблицы
     *
     * @param author объект автор
     * @return
     */
    private String[] authorToRow(Author author) {
        String idCell = author.getId().toString();
        String name = author.getName();
        String realName = author.getRealName() == null ? "" : author.getRealName();
        String birthdayCell = birthdayToString(author.getBirthday());
        return new String[]{idCell, name, realName, birthdayCell};
    }

    /**
     * Преобразование дня рождения в строку
     *
     * @param date
     * @return
     */
    private String birthdayToString(LocalDate date) {
        if (date == null)
            return DEFAULT_EMPTY_VALUE;
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
