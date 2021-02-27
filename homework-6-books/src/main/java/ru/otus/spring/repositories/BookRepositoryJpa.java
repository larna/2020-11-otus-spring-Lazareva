package ru.otus.spring.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

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

    @Transactional
    @Override
    public Book save(Book book) {
        if (book.getId() == null || !existsById(book.getId())) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Transactional
    @Override
    public Book updateBookName(Long bookId, String newBookName) {
        em.createQuery("UPDATE Book b SET b.name=:bookName WHERE b.id=:id")
                .setParameter("bookName", newBookName)
                .setParameter("id", bookId)
                .executeUpdate();
        return findBookById(bookId);
    }

    @Transactional
    @Override
    public void delete(Book book) {
        em.remove(book);
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean existsById(Long id) {
        TypedQuery<Boolean> query = em.createQuery("SELECT EXISTS(SELECT 1 FROM Book b WHERE b.id=:id) FROM Book b",
                Boolean.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean existsBookByIsbn(String isbn) {
        TypedQuery<Boolean> query = em.createQuery("SELECT EXISTS(SELECT 1 FROM Book b WHERE b.isbn=:isbn) FROM Book b",
                Boolean.class);
        query.setParameter("isbn", isbn);
        return query.getSingleResult();
    }

    /**
     * Я вначале сделала работу с spring data jpa и оттуда пришел Optional для findById(id)
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findById(Long id) {
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.id=:id", Book.class)
                .setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Transactional(readOnly = true)
    @Override
    public Book findBookByIsbn(String isbn) {
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.isbn=:isbn", Book.class);
        query.setParameter("isbn", isbn);
        return query.getSingleResult();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAllByAuthorsIs(Author author) {
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b INNER JOIN b.authors a WHERE a=:author", Book.class);
        query.setParameter("author", author);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAllByGenre(Genre genre) {
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.genre=:genre", Book.class);
        query.setParameter("genre", genre);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Book> findAll(Pageable pageable) {
        TypedQuery<Long> countQuery = em.createQuery("SELECT count(*) FROM Book b", Long.class);
        Long count = countQuery.getSingleResult();
        if (count == 0)
            return Page.empty();

        List<Book> books = em.createQuery("SELECT b FROM Book b INNER JOIN fetch b.genre g " +
                "INNER JOIN fetch b.authors a ORDER BY b.name", Book.class)
                .setFirstResult(Long.valueOf(pageable.getOffset()).intValue())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(books, pageable, count);
    }

    @Override
    public List<Book> findAll() {
        EntityGraph entityGraph = em.getEntityGraph("books-genre-authors-entity-graph");
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b", Book.class)
                .setHint("javax.persistence.fetchgraph", entityGraph);
        List<Book> books = query.getResultList();
        return books;
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public Book findBookById(Long id) {
        return em.createQuery("SELECT b FROM Book b INNER JOIN fetch b.genre g " +
                "INNER JOIN fetch b.authors a WHERE b.id=:id", Book.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
