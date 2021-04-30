package ru.otus.spring.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.controller.ui.select.BookSelect;
import ru.otus.spring.controller.ui.views.View;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.services.comments.CommentService;

import java.util.List;
import java.util.Optional;

/**
 * Компонент, обслуживающий работу интерфейса с книгами
 */
@ShellComponent
@ShellCommandGroup("comment")
public class CommentCommands {
    /**
     * Сервис для работы с комментариями
     */
    private final CommentService service;
    /**
     * Отображение комментариев пользователю
     */
    private final View<Comment> commentView;
    private final View<Book> bookView;

    private final BookSelect bookSelect;

    public CommentCommands(CommentService service,
                           @Qualifier("commentsView") View<Comment> commentView,
                           @Qualifier("booksView") View<Book> bookView,
                           BookSelect bookSelect) {
        this.service = service;
        this.commentView = commentView;
        this.bookView = bookView;
        this.bookSelect = bookSelect;
    }


    /**
     * вывод комментариев
     *
     * @return
     */
    @ShellMethod(key = {"c", "comments"}, value = "View all comments")
    public String findAllBooks() {
        List<Comment> comments = service.findAll();
        return show(comments);
    }

    /**
     * Добавить новый комментарий
     *
     * @return
     */
    @ShellMethod(key = {"c+", "comment-insert"}, value = "New comment")
    public String newBook(@ShellOption(value = {"-t", "-text"}, help = "Text of comment") String text) {
        try {
            Optional<Book> selectedBook = bookSelect.prompt();
            if (!selectedBook.isPresent())
                return "Книга не выбрана!";

            Comment comment = Comment.builder().description(text).book(selectedBook.get()).build();
            service.save(comment);
            return commentView.getObjectView(comment, "Комментарий успешно добавлен");
        } catch (Exception e) {
            return "Комментарий не удалось добавить. Проверьте есть ли выбранная вами книга...";
        }
    }

    /**
     * Добавить новый комментарий
     *
     * @return
     */
    @ShellMethod(key = {"c?", "comment-view"}, value = "Comment view for book")
    public String findCommentsByBook() {
        Optional<Book> selectedBook = bookSelect.prompt();
        if (!selectedBook.isPresent())
            return "Книга не выбрана!";

        List<Comment> comments = service.findByBookId(selectedBook.get().getId());
        return commentView.getListView(comments, "Комментарии для книги: " + selectedBook.get().getName());
    }

    /**
     * Показать список пользователю
     *
     * @param comments
     * @return
     */
    private String show(List<Comment> comments) {
        if (comments.isEmpty())
            return "Комментарии не найдены";

        String message = String.format("Показано %d", comments.size());
        return commentView.getListView(comments, message);
    }
}
