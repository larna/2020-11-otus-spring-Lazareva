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

    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = em.createQuery("SELECT g FROM Genre g ORDER BY g.name", Genre.class);
        return query.getResultList();
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }
}
