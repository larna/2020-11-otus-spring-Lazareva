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

    @Transactional(readOnly = true)
    @Override
    public List<Author> findAllByNameLike(String authorName) {
        TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a " +
                "WHERE a.name like :name ORDER BY a.name ASC", Author.class)
                .setParameter("name", "%" + authorName + "%");
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Author> findAllByIdIn(List<Long> idList) {
        TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a " +
                "WHERE a.id IN (:idArray) ORDER BY a.name ASC", Author.class)
                .setParameter("idArray", idList);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Author> findAll(Pageable pageable) {
        TypedQuery<Long> countQuery = em.createQuery("SELECT count(*) FROM Author a", Long.class);
        Long count = countQuery.getSingleResult();
        TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a ORDER BY a.name ASC", Author.class)
                .setFirstResult(Long.valueOf(pageable.getOffset()).intValue())
                .setMaxResults(pageable.getPageSize());
        List<Author> authors = query.getResultList();
        return new PageImpl<Author>(authors, pageable, count);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Author> findAll(AuthorSearchSpecification spec, Pageable pageable) {
        AuthorQueryBuilder queryBuilder = spec.execute();
        final String jpql = queryBuilder.buildCountQuery();
        TypedQuery<Long> countQuery = em.createQuery(jpql, Long.class);
        HashMap<String, Object> params = queryBuilder.getParameters();
        params.keySet().stream()
                .forEach(paramName ->
                        countQuery.setParameter(paramName, params.get(paramName)));
        Long count = countQuery.getSingleResult();
        if (count == 0)
            return Page.empty();

        queryBuilder.orderBy();
        TypedQuery<Author> query = em.createQuery(queryBuilder.build(), Author.class)
                .setFirstResult(Long.valueOf(pageable.getOffset()).intValue())
                .setMaxResults(pageable.getPageSize());
        params.keySet().stream().forEach(paramName -> query.setParameter(paramName, params.get(paramName)));
        List<Author> authors = query.getResultList();

        return new PageImpl<Author>(authors, pageable, count);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Author> findById(Long id) {
        TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a WHERE a.id = :id",
                Author.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Transactional
    @Override
    public Author save(Author author) {
        if (author.getId() == null || !existsById(author.getId())) {
            em.persist(author);
            return author;
        }
        return em.merge(author);
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean existsById(Long id) {
        TypedQuery<Boolean> query = em.createQuery("SELECT EXISTS(SELECT 1 FROM Author a WHERE a.id=:id) FROM Author a", Boolean.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Query query = em.createQuery("DELETE FROM Author a WHERE a.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
