package ru.otus.spring.controller.ui;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.controller.dto.BookDto;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.List;
import java.util.Optional;

/**
 * Представление для отображения комментариев
 */
@Component("commentsView")
@RequiredArgsConstructor
public class CommentsViewConsoleTable implements View<Comment> {
    /**
     * Заголовок таблицы
     */
    private static final String[] TABLE_HEADER_ROW = new String[]{"ID", "Текст", "Книга"};

    /**
     * Показать список комментариев
     *
     * @param comments список комментариев
     * @param message  сообщение
     * @return
     */
    @Override
    public String getListView(List<Comment> comments, String message) {
        if (comments == null || comments.size() == 0)
            return "Книги не найдены...";

        String result = getCommentsTable(comments).render();
        return String.format("%s\n %s\n", result, message);
    }

    /**
     * Показать один комментарий
     *
     * @param comment объект комментарий
     * @param message сообщение пользователю
     * @return
     */
    @Override
    public String getObjectView(Comment comment, String message) {
        if (comment == null)
            throw new IllegalArgumentException("Для отображения автора передали нулевой объект");
        return getListView(List.of(comment), message);
    }

    /**
     * Заполнение таблицы
     *
     * @param books
     * @return
     */
    private AsciiTable getCommentsTable(List<Comment> comments) {
        final AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow(TABLE_HEADER_ROW);
        table.addRule();
        comments.stream().forEach(comment -> {
            table.addRow(commentToRow(comment));
            table.addRule();
        });
        table.getRenderer().setCWC(new CWC_LongestWordMin(new int[]{-1, 30, 30}));
        return table;

    }

    /**
     * Получить строку таблицы
     *
     * @param comment
     * @return
     */
    private String[] commentToRow(Comment comment) {
        String idCell = Long.valueOf(comment.getId()).toString();
        String text = comment.getDescription();
        String book = comment.getBook().getName();
        return new String[]{idCell, text, book};
    }

//    private String bookToString(Book book) {
//        StringBuffer text = new StringBuffer();
//        text.append(book.getName()).append(" - ");
//        text.append(book.getGenre().getName()).append(" - ");
//        String authors = book.getAuthors().stream()
//                .map(author -> author.getName())
//                .reduce((s1, s2) -> s1 + ", " + s2)
//                .orElse("авторы не найдены");
//        text.append(authors);
//        return text.toString();
//    }
}
