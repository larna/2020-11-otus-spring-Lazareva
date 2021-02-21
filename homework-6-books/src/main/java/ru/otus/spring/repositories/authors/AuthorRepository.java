package ru.otus.spring.repositories.authors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

import java.time.LocalDate;
import java.util.List;

/**
 * Репозиторий для работы с авторами
 */
public interface AuthorRepository {
    /**
     * Новый автор
     *
     * @param author объект автор
     * @return автора
     */
    Author insert(Author author);

    /**
     * Изменить автора
     *
     * @param author объект автор
     * @return автора
     */
    Author update(Author author);

    /**
     * Удалить автора
     *
     * @param id идентиифкатор автора
     */
    void delete(Long id);

    /**
     * Проверить существование атвора с заданным id
     *
     * @param id идентиифкатор автора
     * @return true - если существует и false - в противном случае
     */
    Boolean isExists(Long id);

    /**
     * Получить автора по id
     *
     * @param id идентиифкатор автора
     * @return автора
     */
    Author getAuthorById(Long id);

    /**
     * Получить автора по псевдониму, имени, дате рождения
     *
     * @param name имя автора или псевдоним
     * @param realName настоящее имя автора
     * @param birthday дата рождения
     * @return автора
     */
    Author getAuthorByNameAndRealNameAndBirthday(String name, String realName, LocalDate birthday);

    /**
     * Получить всех авторов для книг
     *
     * @param books список книг
     * @return список авторов
     */
    List<Author> getAllByBooks(List<Book> books);

    /**
     * Получить авторов по имени
     *
     * @param name имя автора
     * @return список авторов
     */
    List<Author> getAuthorByLikeName(String name);

    /**
     * По-страничный вывод всех авторов
     *
     * @param pageable параметры страницы
     * @return страницу найденных авторов
     */
    Page<Author> getAll(Pageable pageable);

    /**
     * По-страничный вывод авторов согласно фильтру
     *
     * @param specification спецификация
     * @param pageable параметры страницы
     * @return страницу найденных авторов
     */
    Page<Author> getAllByFilter(AuthorSearchSpecification specification, Pageable pageable);
}
