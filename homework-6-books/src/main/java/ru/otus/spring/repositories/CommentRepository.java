package ru.otus.spring.repositories;

import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Comment;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с комментариями
 */
@Repository
public interface CommentRepository {
    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

    Boolean existsById(Long id);

    void delete(Comment comment);

    List<Comment> findAllByBook_Id(Long bookId);

    List<Comment> findAll();

}
