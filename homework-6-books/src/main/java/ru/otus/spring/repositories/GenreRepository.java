package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;
/**
 * Репозиторий для работы с жанрами
 */
@Repository
public interface GenreRepository {
    List<Genre> findAll();

    Optional<Genre> findById(Long id);
}
