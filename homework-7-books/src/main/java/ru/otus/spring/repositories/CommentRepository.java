package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Comment;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с комментариями
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Поиск по идентиифкатору книги
     * @param bookId
     * @return
     */
    List<Comment> findAllByBook_Id(Long bookId);

    /**
     * Найти все комментарии
     * @return
     */
    @EntityGraph(attributePaths = {"book"})
    List<Comment> findAll();

    /**
     * Найти комментарий по id
     * @param id
     * @return
     */
    @EntityGraph(attributePaths = {"book"})
    Comment findCommentById(Long id);

}
