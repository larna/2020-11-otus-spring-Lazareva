package ru.otus.spring.repositories;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Book;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с книгами
 */
@Repository
public class BookRepositoryJpa implements BookRepository {
    @PersistenceContext
    EntityManager em;

    /**
     * Сохранение книги
     *
     * @param book объект книги для сохранения
     * @return объект сохраненной книги
     */
    @Override
    public Book save(Book book) {
        if (book.getId() == null || !existsById(book.getId())) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    /**
     * Удаление книги
     *
     * @param book объект книги для удаления
     */
    @Override
    public void delete(Book book) {
        em.remove(book);
    }

    /**
     * Проверка книги на существование
     *
     * @param id идентиифкатор книги
     * @return true - если книга существует, false - в противном случае
     */
    @Override
    public Boolean existsById(Long id) {
        TypedQuery<Boolean> query = em.createQuery("SELECT EXISTS(SELECT 1 FROM Book b WHERE b.id=:id) FROM Book b",
                Boolean.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    /**
     * Найти книгу по id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Book> findById(Long id) {
        try {
            Book book = em.createQuery("SELECT b FROM Book b INNER JOIN fetch b.genre g " +
                    "INNER JOIN fetch b.authors a WHERE b.id=:id", Book.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.ofNullable(book);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * Найти все книги. По-страничный вывод
     *
     * @param pageable
     * @return страницу книг
     */
    @Override
    public Page<Book> findAll(Pageable pageable) {
        TypedQuery<Long> countQuery = em.createQuery("SELECT count(*) FROM Book b", Long.class);
        Long count = countQuery.getSingleResult();
        if (count == 0)
            return Page.empty();

        List<Book> books = em.createQuery("SELECT distinct b FROM Book b INNER JOIN fetch b.genre g " +
                "INNER JOIN fetch b.authors a ORDER BY b.name", Book.class)
                .setFirstResult(Long.valueOf(pageable.getOffset()).intValue())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(books, pageable, count);
    }

    /**
     * Найти книги по фильтру
     *
     * @param spec
     * @param pageable
     * @return
     */
    @Override
    public Page<Book> findAll(BookSearchSpecification spec, Pageable pageable) {
        BookQueryBuilder queryBuilder = spec.execute();
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
        EntityGraph entityGraph = em.getEntityGraph("books-genre-authors-entity-graph");
        TypedQuery<Book> query = em.createQuery(queryBuilder.build(), Book.class)
                .setFirstResult(Long.valueOf(pageable.getOffset()).intValue())
                .setMaxResults(pageable.getPageSize())
                .setHint("javax.persistence.fetchgraph", entityGraph);
        params.keySet().stream().forEach(paramName -> query.setParameter(paramName, params.get(paramName)));
        List<Book> books = query.getResultList();

        return new PageImpl<Book>(books, pageable, count);
    }
}
