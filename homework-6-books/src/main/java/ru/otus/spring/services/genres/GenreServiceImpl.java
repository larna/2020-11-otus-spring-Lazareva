package ru.otus.spring.services.genres;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.GenreRepository;

import java.util.List;

/**
 * Сервис для работы с жанрами
 */
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    /**
     * Репозиторий для работы с жанрами
     */
    private final GenreRepository repository;

    /**
     * Показать все жанры
     *
     * @return список жанров
     */
    @Transactional(readOnly = true)
    @Override
    public List<Genre> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Genre findById(Long id) {
        return repository.findById(id)
                .orElseThrow(GenreNotFoundException::new);
    }
}
