package ru.otus.spring.controller.ui.views;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Представление для отображения книг
 */
@Component("booksView")
@RequiredArgsConstructor
public class BooksViewConsoleTable implements View<Book> {
    /**
     * Заголовок таблицы
     */
    private static final String[] TABLE_HEADER_ROW = new String[]{"Номер", "Название книги", "ISBN", "Авторы", "Жанр", "ID"};

    /**
     * Показать список книг
     *
     * @param books   список книг
     * @param message сообщение
     * @return
     */
    @Override
    public String getListView(List<Book> books, String message) {
        if (books == null || books.size() == 0)
            return "Книги не найдены...";

        String result = getBooksTable(books).render();
        return String.format("%s\n %s\n", result, message);
    }

    /**
     * Показать одну книгу
     *
     * @param book    объект книга
     * @param message сообщение пользователю
     * @return
     */
    @Override
    public String getObjectView(Book book, String message) {
        if (book == null)
            throw new IllegalArgumentException("Для отображения автора передали нулевой объект");
        return getListView(List.of(book), message);
    }

    /**
     * Заполнение таблицы
     *
     * @param books
     * @return
     */
    private AsciiTable getBooksTable(List<Book> books) {
        final AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow(TABLE_HEADER_ROW);
        table.addRule();
        IntStream.range(0, books.size()).forEach(i -> {
            table.addRow(bookToRow(books.get(i), i + 1));
            table.addRule();
        });
        table.getRenderer().setCWC(new CWC_LongestWordMin(new int[]{-1, 30, 20, 30, 15, -1}));
        return table;

    }

    /**
     * Получить строку таблицы
     *
     * @param book
     * @return
     */
    private String[] bookToRow(Book book, Integer number) {
        String numCell = number.toString();
        String name = book.getName();
        String isbn = Optional.ofNullable(book.getIsbn()).orElse("-");
        String authors = book.getAuthors().stream()
                .map(Author::getName)
                .collect(Collectors.joining(", "));
        String genre = book.getGenre() == null ? "Жанр для книги не найден" : book.getGenre().getName();
        String id = book.getId();
        return new String[]{numCell, name, isbn, authors, genre, id};
    }
}
