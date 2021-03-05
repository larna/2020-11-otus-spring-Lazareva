package ru.otus.spring.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Comment;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с комментариями
 */
@Repository
public class CommentRepositoryJpa implements CommentRepository {
    @PersistenceContext
    EntityManager em;

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null || !existsById(comment.getId())) {
            em.persist(comment);
            return comment;
        }
        return em.merge(comment);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public Boolean existsById(Long id) {
        TypedQuery<Boolean> query = em.createQuery("SELECT EXISTS(SELECT 1 FROM Comment c WHERE c.id= :id) FROM Comment c",
                Boolean.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public void delete(Comment comment) {
        em.remove(comment);
    }

    @Override
    public List<Comment> findAllByBook_Id(Long bookId) {
        TypedQuery<Comment> query = em.createQuery("SELECT c FROM Comment c INNER JOIN c.book b WHERE b.id= :bookId",
                Comment.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

    @Override
    public List<Comment> findAll() {
        return em.createQuery("SELECT c FROM Comment c", Comment.class).getResultList();
    }
}
