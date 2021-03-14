package ru.otus.spring.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Book;
import java.util.Optional;

/**
 * Репозиторий для работы с книгами
 */
@Repository
public interface BookRepository {
    /**
     * Сохранение книги
     * @param book объект книги для сохранения
     * @return объект сохраненной книги
     */
    Book save(Book book);

    /**
     * Удаление книги
     * @param book объект книги для удаления
     */
    void delete(Book book);

    /**
     * Проверка книги на существование
     * @param id идентиифкатор книги
     * @return true - если книга существует, false - в противном случае
     */
    Boolean existsById(Long id);

    /**
     * Найти книгу по id
     * @param id
     * @return
     */
    Optional<Book> findById(Long id);

    /**
     * Найти все книги. По-страничный вывод
     * @param pageable
     * @return страницу книг
     */
    Page<Book> findAll(Pageable pageable);

    /**
     * Найти книги по фильтру
     * @param spec
     * @param pageable
     * @return
     */
    Page<Book> findAll(BookSearchSpecification spec, Pageable pageable);
}
