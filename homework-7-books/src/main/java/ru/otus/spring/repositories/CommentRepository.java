package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBook_Id(Long bookId);

    @EntityGraph(attributePaths = {"book"})
    List<Comment> findAll();

    @EntityGraph(attributePaths = {"book"})
    Comment findCommentById(Long id);

}
