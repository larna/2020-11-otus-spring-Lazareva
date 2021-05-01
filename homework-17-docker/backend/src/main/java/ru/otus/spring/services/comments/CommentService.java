package ru.otus.spring.services.comments;

import ru.otus.spring.domain.Comment;

import java.util.List;

/**
 * Сервис для работы с комментариями
 */
public interface CommentService {
    Comment save(Comment comment);

    Comment findById(String commentId);
    List<Comment> findByBookId(String bookId);

    List<Comment> findAll();
    void delete(String id);
}
