package ru.otus.spring.services.authors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repositories.AuthorSearchSpecification;

import java.util.List;

/**
 * Сервис для работы с авторами
 */
@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repository;

    /**
     * Найти всех авторов. По-страничный вывод.
     *
     * @param pageable параметры по-страничного вывода
     * @return страница найденных авторов
     */
    @Transactional(readOnly = true)
    @Override
    public Page<Author> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Author> findAllByIdIn(List<Long> authorsId) {
        return repository.findAllByIdIn(authorsId);
    }

    /**
     * Найти авторов согласно фильтру
     *
     * @param filter   фильтр
     * @param pageable параметры по-страничного вывода
     * @return страница найденных авторов
     */
    @Transactional(readOnly = true)
    @Override
    public Page<Author> findAllByFilter(SearchFilter filter, Pageable pageable) {
        if (filter.isEmpty())
            return repository.findAll(pageable);

        Specification spec = (Specification) new AuthorSearchSpecification(filter);
        return repository.findAll(spec, pageable);
    }

    /**
     * Найти автора по id
     *
     * @param authorId id автора
     * @return объект автора
     * @throws AuthorNotFoundException если автор не существует будет выброщено исключение
     */
    @Transactional(readOnly = true)
    @Override
    public Author findById(Long authorId) {
        return repository.findById(authorId).orElseThrow(AuthorNotFoundException::new);
    }

    /**
     * Найти авторов по имени. Поиск производиться с помощью оператора like '%введенная часть имени%'
     * Поиск чувствителен к регистру.
     *
     * @param name имя автора или его часть
     * @return список авторов
     */
    @Transactional(readOnly = true)
    @Override
    public List<Author> findByName(String name) {
        return repository.findAllByNameLike(name);
    }

    /**
     * Сохранить автора
     *
     * @param author объект автор
     * @return сохраненный автор
     */
    @Transactional
    @Override
    public Author save(Author author) {
        return repository.save(author);
    }

    /**
     * Удалить автора по id
     *
     * @param authorId id автора
     * @throws AuthorNotFoundException если автор не существует будет выброщено исключение
     */
    @Transactional
    @Override
    public void deleteById(Long authorId) {
        if (!repository.existsById(authorId)) {
            throw new AuthorNotFoundException();
        }
        repository.deleteById(authorId);
    }
}
