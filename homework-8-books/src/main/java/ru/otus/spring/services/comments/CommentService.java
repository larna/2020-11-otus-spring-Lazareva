package ru.otus.spring.services.comments;

import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.List;

/**
 * Сервис для работы с комментариями
 */
public interface CommentService {
    Comment save(Comment comment);

    List<Comment> findByBookId(String bookId);

    List<Comment> findAll();
}
