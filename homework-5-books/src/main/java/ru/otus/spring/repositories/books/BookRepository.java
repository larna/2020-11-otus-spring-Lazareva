package ru.otus.spring.repositories.books;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;

/**
 * Репозиторий для работы с книгами
 */
public interface BookRepository {
    /**
     * Создать новую книгу
     *
     * @param book объект книга
     * @return объект книги
     */
    Book insert(Book book);

    /**
     * Изменить книгу
     *
     * @param book объект книга
     * @return объект книги
     */
    Book update(Book book);

    /**
     * Получить книгу по идентификатору
     *
     * @param bookId идентификтор книги
     * @return объект книги вместе с авторами и жанром
     */
    Book getById(long bookId);

    Book getByIsbn(String isbn);

    /**
     * Получить книгу по названию и жанру
     *
     * @param genre жанр
     * @return объект книги вместе с авторами и жанром
     */
    List<Book> getAllByGenre(Genre genre);

    List<Book> getAllByAuthor(Author author);

    Page<Book> getAll(Pageable pageable);

    /**
     * Получить по-страничный список книг согласно спецификации
     *
     * @param specification
     * @param pageable
     * @return
     */
    Page<Book> getAll(BookSearchSpecification specification, Pageable pageable);

    /**
     * Получить по-страничный список книг у которых имя содержит подстроку
     *
     * @param bookName
     * @return страницу со списком книг
     */
    Page<Book> getAllByLikeBookName(String bookName, Pageable pageable);

    /**
     * Получить по-страничный список книг по имени автора
     *
     * @param authorName
     * @return страницу со списком книг
     */
    Page<Book> getAllByLikeAuthorName(String authorName, Pageable pageable);

    /**
     * Получить по-страничный список книг по названию жанра
     *
     * @param genreName
     * @return страницу со списком книг
     */
    Page<Book> getAllByLikeGenreName(String genreName, Pageable pageable);

    /**
     * Получить по-страничный список книг по имени автора,  названию жанра, названию книги
     *
     * @param authorName
     * @param genreName
     * @param bookName
     * @return страницу со списком книг
     */
    Page<Book> getAllByLikeAuthorNameAndLikeGenreNameAndLikeBookName(String authorName, String genreName, String bookName, Pageable pageable);

    /**
     * Получить по-страничный список книг по имени автора,  названию книги
     *
     * @param authorName
     * @param bookName
     * @param pageable
     * @return
     */
    Page<Book> getAllByLikeAuthorNameAndLikeBookName(String authorName, String bookName, Pageable pageable);

    /**
     * Получить по-страничный список книг по имени автора,  названию жанра
     *
     * @param authorName
     * @param genreName
     * @param pageable
     * @return
     */
    Page<Book> getAllByLikeAuthorNameAndLikeGenreName(String authorName, String genreName, Pageable pageable);

    /**
     * Получить по-страничный список книг по названию жанра, названию книги
     *
     * @param genreName
     * @param bookName
     * @param pageable
     * @return
     */
    Page<Book> getAllByLikeGenreNameAndLikeBookName(String genreName, String bookName, Pageable pageable);

    /**
     * Проверка существования книги по isbn
     *
     * @param isbn
     * @return true - существует, false - не существует
     */
    boolean isExistsByIsbn(String isbn);

    /**
     * Проверка существования книги по идентификатору
     *
     * @param bookId идентификатор книги
     * @return true - существует, false - не существует
     */
    Boolean isExistsById(long bookId);

    /**
     * Удалить книгу по её идентификатору
     *
     * @param bookId удалить книгу
     */
    void delete(long bookId);
}
