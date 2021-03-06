package ru.otus.spring.services.authors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repositories.AuthorRepository;

import java.util.List;

/**
 * Сервис для работы с авторами
 */
@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repository;

    /**
     * Найти всех авторов.
     *
     * @return список авторов
     */
    @Transactional(readOnly = true)
    @Override
    public List<Author> findAll() {
        return repository.findAll();
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

}
