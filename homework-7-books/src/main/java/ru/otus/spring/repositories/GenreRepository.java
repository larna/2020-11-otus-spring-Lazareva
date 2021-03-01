package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Genre;

/**
 * Репозиторий для работы с жанрами
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
