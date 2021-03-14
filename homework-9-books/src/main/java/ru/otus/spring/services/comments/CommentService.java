package ru.otus.spring.services.comments;

import ru.otus.spring.domain.Comment;

import java.util.List;

/**
 * Сервис для работы с комментариями к книге
 */
public interface CommentService {
    /**
     * Сохранить комментарий
     *
     * @param comment
     * @return
     */
    Comment save(Comment comment);

    /**
     * Найти по id
     *
     * @param id
     * @return
     */
    Comment findById(Long id);

    /**
     * Найти все комментарии
     *
     * @return
     */
    List<Comment> findAll();

    /**
     * Удалить комментарий
     *
     * @param idComment
     */
    void deleteById(Long idComment);
}
