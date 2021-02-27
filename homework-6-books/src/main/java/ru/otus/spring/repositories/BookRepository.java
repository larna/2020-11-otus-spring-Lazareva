package ru.otus.spring.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с книгами
 */
@Repository
public interface BookRepository {
    Book save(Book book);

    Book updateBookName(Long bookId, String newBookName);

    void delete(Book book);

    Boolean existsById(Long id);

    Boolean existsBookByIsbn(String isbn);

    Optional<Book> findById(Long id);

    Book findBookByIsbn(String isbn);

    List<Book> findAllByAuthorsIs(Author author);

    List<Book> findAllByGenre(Genre genre);

    Page<Book> findAll(Pageable pageable);
    List<Book> findAll();

    Page<Book> findAll(BookSearchSpecification spec, Pageable pageable);

    Book findBookById(Long id);
}
