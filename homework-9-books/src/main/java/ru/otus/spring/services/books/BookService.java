package ru.otus.spring.services.books;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.controller.dto.BookDto;
import ru.otus.spring.domain.Book;

/**
 * Сервис для работы с книгами
 */
public interface BookService {
    /**
     * Сохранить книгу
     *
     * @param book объект книга
     * @return сохраненный объект книги
     */
    Book save(Book book);

    /**
     * Изменить название книги
     *
     * @param bookId   id книги
     * @param bookName новое название
     * @return измененный объект книги
     * @throws BookNotFoundException при попытке изменить несуществующую книгу будет выброщено исключение
     */
    Book updateBookName(Long bookId, String bookName) throws BookNotFoundException;

    /**
     * Удалить книгу
     *
     * @param bookId id книги
     * @throws BookNotFoundException при попытке удалить несуществующую книгу будет выброщено исключение
     */
    void delete(Long bookId) throws BookNotFoundException;

    /**
     * Найти книгу по id
     *
     * @param bookId id книги
     * @return книгу
     * @throws BookNotFoundException если книга не найдена будет выброщено исключение
     */
    Book findById(Long bookId) throws BookNotFoundException;

    /**
     * По-страничный поиск всех книг
     * @param pageable параметры по-страничного поиска
     * @return страница найденных книг
     */
    Page<Book> findAll(Pageable pageable);

    /**
     * По-страничный поиск книг по фильтру
     * @param filter фильр
     * @param pageable параметры по-страничного поиска
     * @return страница найденных книг
     */
    Page<Book> findAllByFilter(SearchFilter filter, Pageable pageable);
    BookDto domainToDto(Book book);
    Book dtoToDomain(BookDto bookDto);
}
