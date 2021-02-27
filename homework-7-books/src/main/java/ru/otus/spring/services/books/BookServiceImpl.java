package ru.otus.spring.services.books;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.BookSearchSpecification;

import java.util.List;

/**
 * Класс реализация сервиса для работы с книгами
 */
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    /**
     * Репозиторий для работы с книгами
     */
    private final BookRepository bookRepository;
    /**
     * Сохранить книгу. Если идентиифкатор книги не задан или книги с таким идентиифкатором не существует,
     * то будет создана новая книга. В противном случае, книга будет отредактирована.
     *
     * @param book объект книга
     * @return сохраненный объект книги
     */
    @Transactional
    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Изменить название книги
     *
     * @param bookId   id книги
     * @param bookName новое название
     * @return измененный объект книги
     * @throws BookNotFoundException при попытке изменить несуществующую книгу будет выброщено исключение
     */
    @Transactional
    @Override
    public Book updateBookName(Long bookId, String bookName) throws BookNotFoundException {
        Book updatingBook = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        updatingBook.setName(bookName);
        bookRepository.save(updatingBook);
        return bookRepository.findBookById(updatingBook.getId());
    }

    /**
     * Удалить книгу
     *
     * @param bookId id книги
     * @throws BookNotFoundException при попытке удалить несуществующую книгу будет выброщено исключение
     */
    @Transactional
    @Override
    public void delete(Long bookId) throws BookNotFoundException {
        if (!bookRepository.existsById(bookId))
            throw new BookNotFoundException();

        bookRepository.deleteById(bookId);
    }

    /**
     * Найти книгу по id
     *
     * @param bookId id книги
     * @return книгу
     * @throws BookNotFoundException если книга не найдена будет выброщено исключение
     */
    @Transactional(readOnly = true)
    @Override
    public Book findById(Long bookId) throws BookNotFoundException {
        if (!bookRepository.existsById(bookId))
            throw new BookNotFoundException();
        return bookRepository.findBookById(bookId);
    }

    /**
     * Найти книгу по isbn
     *
     * @param isbn International Standard Book Number
     * @return найденную книгу
     * @throws BookNotFoundException если книга не найдена будет выброщено исключение
     */
    @Transactional(readOnly = true)
    @Override
    public Book findByIsbn(String isbn) throws BookNotFoundException {
        if (!bookRepository.existsBookByIsbn(isbn))
            throw new BookNotFoundException();

        return bookRepository.findBookByIsbn(isbn);
    }

    /**
     * Найти книги по автору
     *
     * @param author объект автор
     * @return список книг
     */
    @Transactional(readOnly = true)
    @Override
    public List<Book> findByAuthor(Author author) {
        return bookRepository.findAllByAuthorsIs(author);
    }

    /**
     * Найти все книги по жанру
     *
     * @param genre объект жанр
     * @return список книг
     */
    @Transactional(readOnly = true)
    @Override
    public List<Book> findByGenre(Genre genre) {
        return bookRepository.findAllByGenre(genre);
    }

    /**
     * По-страничный поиск всех книг
     *
     * @param pageable параметры по-страничного поиска
     * @return страница найденных книг
     */
    @Transactional(readOnly = true)
    @Override
    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    /**
     * По-страничный поиск книг по фильтру
     *
     * @param filter   фильр
     * @param pageable параметры по-страничного поиска
     * @return страница найденных книг
     */
    @Transactional(readOnly = true)
    @Override
    public Page<Book> findAllByFilter(SearchFilter filter, Pageable pageable) {
        Specification specification = (Specification) new BookSearchSpecification(filter);
        return bookRepository.findAll(specification, pageable);
    }
}
