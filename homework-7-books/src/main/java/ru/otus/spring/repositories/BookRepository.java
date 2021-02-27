package ru.otus.spring.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Book findBookByIsbn(String isbn);

    Boolean existsBookByIsbn(String isbn);

    List<Book> findAllByAuthorsIs(Author author);

    List<Book> findAllByGenre(Genre genre);

    @EntityGraph(attributePaths = {"authors", "genre"})
    Page<Book> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"authors", "genre"})
    Page<Book> findAll(Specification spec, Pageable pageable);

    @EntityGraph(attributePaths = {"authors", "genre"})
    Book findBookById(Long id);

    @Modifying
    @Query("UPDATE Book b SET b.name=:bookName WHERE b.id=:bookId")
    void updateBookName(Long bookId, String bookName);
}
