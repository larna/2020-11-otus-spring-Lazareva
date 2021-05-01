package ru.otus.spring.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с авторами
 */
@Repository
public class AuthorRepositoryJpa implements AuthorRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Author> findAllByNameLike(String authorName) {
        TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a " +
                "WHERE a.name like :name ORDER BY a.name ASC", Author.class)
                .setParameter("name", "%" + authorName + "%");
        return query.getResultList();
    }

    @Override
    public List<Author> findAllByIdIn(List<Long> idList) {
        TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a " +
                "WHERE a.id IN (:idArray) ORDER BY a.name ASC", Author.class)
                .setParameter("idArray", idList);
        return query.getResultList();
    }

    @Override
    public List<Author> findAll() {
        return em.createQuery("SELECT a FROM Author a ORDER BY a.name ASC", Author.class)
                .getResultList();
    }

    @Override
    public Optional<Author> findById(Long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == null || !existsById(author.getId())) {
            em.persist(author);
            return author;
        }
        return em.merge(author);
    }

    @Override
    public Boolean existsById(Long id) {
        TypedQuery<Boolean> query = em.createQuery("SELECT EXISTS(SELECT 1 FROM Author a WHERE a.id=:id) FROM Author a", Boolean.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public void delete(Author author) {
        em.remove(author);
    }
}
