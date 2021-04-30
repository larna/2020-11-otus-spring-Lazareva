package ru.otus.spring.services.books;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Book;
import ru.otus.spring.repositories.BookRepository;

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
     * Удалить книгу
     *
     * @param book книга которую требуется удалить
     */
    @Transactional
    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
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
        return bookRepository.findAllByFilter(filter, pageable);
    }
}
