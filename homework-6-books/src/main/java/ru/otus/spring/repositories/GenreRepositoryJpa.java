package ru.otus.spring.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с жанрами
 */
@Repository
public class GenreRepositoryJpa implements GenreRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = em.createQuery("SELECT g FROM Genre g ORDER BY g.name", Genre.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Genre> findById(Long id) {
        TypedQuery<Genre> query = em.createQuery("SELECT g FROM Genre g WHERE g.id = :id", Genre.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }
}
