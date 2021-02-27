package ru.otus.spring.services.comments;

import ru.otus.spring.domain.Comment;

import java.util.List;

public interface CommentService {
    Comment save(Comment comment);

    List<Comment> findByBook(Long bookId);

    Comment findById(Long id);

    List<Comment> findAll();

    void deleteById(Long idComment);
}
