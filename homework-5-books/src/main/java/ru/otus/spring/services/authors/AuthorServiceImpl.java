package ru.otus.spring.services.authors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.repositories.authors.AuthorRepository;
import ru.otus.spring.repositories.authors.AuthorSearchSpecification;
import ru.otus.spring.domain.Author;

import java.util.List;

/**
 * Сервис для работы с авторами
 */
@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorDao;

    /**
     * Найти всех авторов. По-страничный вывод.
     *
     * @param pageable параметры по-страничного вывода
     * @return страница найденных авторов
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Author> findAll(Pageable pageable) {
        return authorDao.getAll(pageable);
    }

    /**
     * Найти авторов согласно фильтру
     *
     * @param filter   фильтр
     * @param pageable параметры по-страничного вывода
     * @return страница найденных авторов
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Author> findAllByFilter(SearchFilter filter, Pageable pageable) {
        AuthorSearchSpecification spec = new AuthorSearchSpecification(filter);
        if (spec.isSatisfiedBy())
            return authorDao.getAllByFilter(spec, pageable);
        else
            return findAll(pageable);
    }

    /**
     * Найти автора по id
     *
     * @param authorId id автора
     * @return объект автора
     * @throws AuthorNotFoundException если автор не существует будет выброщено исключение
     */
    @Override
    @Transactional(readOnly = true)
    public Author findById(Long authorId) {
        if (authorDao.isExists(authorId))
            return authorDao.getAuthorById(authorId);
        throw new AuthorNotFoundException();
    }

    /**
     * Найти авторов по имени. Поиск производиться с помощью оператора like '%введенная часть имени%'
     * Поиск чувствителен к регистру.
     *
     * @param name имя автора или его часть
     * @return список авторов
     */
    @Override
    @Transactional(readOnly = true)
    public List<Author> findByName(String name) {
        return authorDao.getAuthorByLikeName(name);
    }

    /**
     * Сохранить автора
     *
     * @param author объект автор
     * @return сохраненный автор
     */
    @Override
    @Transactional
    public Author save(Author author) {
        if (author.getId() == null)
            return authorDao.insert(author);
        return authorDao.update(author);
    }

    /**
     * Удалить автора по id
     *
     * @param authorId id автора
     * @throws AuthorNotFoundException если автор не существует будет выброщено исключение
     */
    @Override
    @Transactional
    public void deleteById(Long authorId) {
        if (!authorDao.isExists(authorId)) {
            throw new AuthorNotFoundException();
        }
        authorDao.delete(authorId);
    }
}
