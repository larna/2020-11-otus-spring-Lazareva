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
 * Представление для отображения книг с комментариями
 */
@Component("bookWithCommentsView")
@RequiredArgsConstructor
public class BookWithCommentsViewConsoleTable implements View<BookDto> {
    /**
     * Заголовок таблицы
     */
    private static final String[] BOOK_TABLE_HEADER_ROW = new String[]{"ID", "Название книги", "ISBN", "Авторы", "Жанр"};
    private static final String[] COMMENT_TABLE_HEADER_ROW = new String[]{"ID", "Текст"};

    /**
     * Показать список книг
     *
     * @param books   список книг
     * @param message сообщение
     * @return
     */
    @Override
    public String getListView(List<BookDto> books, String message) {
        throw new UnsupportedOperationException();
    }

    /**
     * Показать одну книгу
     *
     * @param bookWithComments объект книга
     * @param message          сообщение пользователю
     * @return
     */
    @Override
    public String getObjectView(BookDto bookWithComments, String message) {
        if (bookWithComments == null)
            throw new IllegalArgumentException("Для отображения автора передали нулевой объект");
        String bookOut = getBooksTable(List.of(bookWithComments)).render();
        String commentOut = getCommentsTable(bookWithComments.getCommentList()).render();
        return String.format("%s\n Комментарии книги:\n%s\n%s", bookOut, commentOut , message);
    }

    /**
     * Заполнение таблицы
     *
     * @param books
     * @return
     */
    private AsciiTable getBooksTable(List<BookDto> books) {
        final AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow(BOOK_TABLE_HEADER_ROW);
        table.addRule();
        books.stream().forEach(book -> {
            table.addRow(bookToRow(book));
            table.addRule();
        });
        table.getRenderer().setCWC(new CWC_LongestWordMin(new int[]{-1, 30, 20, 30, 15}));
        return table;

    }

    /**
     * Получить строку таблицы
     *
     * @param book
     * @return
     */
    private String[] bookToRow(BookDto book) {
        String idCell = Long.valueOf(book.getBook().getId()).toString();
        String name = book.getBook().getName();
        String isbn = Optional.ofNullable(book.getBook().getIsbn()).orElse("-");
        String authors = book.getBook().getAuthors().stream()
                .map(Author::getName)
                .reduce((author1, author2) -> author1 + ", " + author2)
                .orElse("Авторы для книги не найдены");
        String genre = book.getBook().getGenre() == null ? "Жанр для книги не найден" : book.getBook().getGenre().getName();
        return new String[]{idCell, name, isbn, authors, genre};
    }

    private AsciiTable getCommentsTable(List<Comment> comments) {
        final AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow(COMMENT_TABLE_HEADER_ROW);
        table.addRule();
        comments.stream().forEach(comment -> {
            table.addRow(commentToRow(comment));
            table.addRule();
        });
        table.getRenderer().setCWC(new CWC_LongestWordMin(new int[]{-1, 30}));
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
        return new String[]{idCell, text};
    }

}
