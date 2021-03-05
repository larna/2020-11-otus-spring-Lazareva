package ru.otus.spring.services.authors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.controller.SearchFilter;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repositories.AuthorSearchSpecification;

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
        AuthorSearchSpecification spec = new AuthorSearchSpecification(filter);
        if (!spec.isSatisfied())
            return repository.findAll(pageable);
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
        Author author = repository.findById(authorId).orElseThrow(AuthorNotFoundException::new);
        repository.delete(author);
    }
}
