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

/**
 * Репозитории для работы с книгами
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    /**
     * Поиск по isbn
     * @param isbn
     * @return
     */
    Book findBookByIsbn(String isbn);

    /**
     * Проверка существования книги по isbn
     * @param isbn
     * @return
     */
    Boolean existsBookByIsbn(String isbn);

    /**
     *  Найти все книги для автора
     * @param author
     * @return
     */
    List<Book> findAllByAuthorsIs(Author author);

    /**
     *  Найти все книги для жанра
     * @param genre
     * @return
     */
    List<Book> findAllByGenre(Genre genre);

    /**
     * найти все книги
     * @param pageable
     * @return
     */
    @EntityGraph(attributePaths = {"authors", "genre"})
    Page<Book> findAll(Pageable pageable);

    /**
     * найти все книги согласно фильтру
     * @param spec
     * @param pageable
     * @return
     */
    @EntityGraph(attributePaths = {"authors", "genre"})
    Page<Book> findAll(Specification spec, Pageable pageable);

    /**
     * найти книги по id
     * @param id
     * @return
     */
    @EntityGraph(attributePaths = {"authors", "genre"})
    Book findBookById(Long id);

    /**
     * изменить название книги
     * @param bookId
     * @param bookName
     */
    @Modifying
    @Query("UPDATE Book b SET b.name=:bookName WHERE b.id=:bookId")
    void updateBookName(Long bookId, String bookName);
}
