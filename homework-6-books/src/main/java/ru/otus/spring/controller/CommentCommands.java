package ru.otus.spring.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.controller.ui.View;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.services.books.BookNotFoundException;
import ru.otus.spring.services.comments.CommentService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public CommentCommands(CommentService service, @Qualifier("commentsView") View<Comment> commentView) {
        this.service = service;
        this.commentView = commentView;
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
    public String newBook(@ShellOption(value = {"-t", "-text"}, help = "Text of comment") String text,
                          @ShellOption(value = {"-b", "-book"}, help = "Id of book") Long bookId) {
        try {
            Book book = Book.builder().id(bookId).build();
            Comment comment = Comment.builder().description(text).book(book).build();
            service.save(comment);
            Comment commentWithBook = service.findById(comment.getId());
            return commentView.getObjectView(commentWithBook, "Комментарий успешно добавлен");
        } catch (Exception e) {
            return "Комментарий не удалось добавить. Проверьте есть ли выбранная вами книга...";
        }
    }

    /**
     * Изменить комментарий
     *
     * @return
     */
    @ShellMethod(key = {"cu", "comment-update"}, value = "Update comment")
    public String updateComment(@ShellOption(value = {"-id"}, help = "Comment id") Long commentId,
                                @ShellOption(value = {"-t", "-text"}, help = "text of comment") String text) {
        try {
            Comment comment = service.findById(commentId);
            comment.setDescription(text);
            service.save(comment);
            Comment commentWithBook = service.findById(comment.getId());
            return commentView.getObjectView(commentWithBook, "Комментарий успешно добавлен");
        } catch (Exception e) {
            return "Комментарий не удалось добавить. Проверьте есть ли выбранная вами книга...";
        }
    }


    /**
     * Удалить комментарий
     *
     * @param commentId id книги
     * @return
     */
    @ShellMethod(key = {"c-", "comment-delete"}, value = "Delete comment by id")
    public String deleteBook(@ShellOption(value = {"-id"}, help = "Delete comment with id") Long commentId) {
        try {
            service.deleteById(commentId);
            return "Комментарий успешно удален";
        } catch (BookNotFoundException e) {
            return "Комментарий не удалось удалить. Комментарий с таким id не найден...";
        }
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
