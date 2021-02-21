package ru.otus.spring.services.authors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.domain.Author;

import java.util.List;

/**
 * Сервис для работы с авторами
 */
public interface AuthorService {
    /**
     * Сохранить автора
     *
     * @param author объект автор
     * @return сохраненный автор
     */
    Author save(Author author);

    /**
     * Удалить автора по id
     * @param authorId id автора
     * @throws AuthorNotFoundException если автор не существует будет выброщено исключение
     */
    void deleteById(Long authorId) throws AuthorNotFoundException;

    /**
     * Найти всех авторов. По-страничный вывод.
     * @param pageable параметры по-страничного вывода
     * @return страница найденных авторов
     */
    Page<Author> findAll(Pageable pageable);

    /**
     * Найти авторов согласно фильтру
     * @param filter фильтр
     * @param pageable параметры по-страничного вывода
     * @return страница найденных авторов
     */
    Page<Author> findAllByFilter(SearchFilter filter, Pageable pageable);

    /**
     * Найти автора по id
     * @param authorId id автора
     * @return объект автора
     * @throws AuthorNotFoundException если автор не существует будет выброщено исключение
     */
    Author findById(Long authorId) throws AuthorNotFoundException;

    /**
     * Найти авторов по имени. Поиск производиться с помощью оператора like '%введенная часть имени%'
     * Поиск чувствителен к регистру.
     *
     * @param name имя автора или его часть
     * @return список авторов
     */
    List<Author> findByName(String name);
}
