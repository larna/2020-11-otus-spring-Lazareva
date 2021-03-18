package ru.otus.spring.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с авторами
 */
@Repository
public interface AuthorRepository {
    List<Author> findAllByNameLike(String authorName);

    List<Author> findAllByIdIn(List<Long> idList);

    List<Author> findAll();

    Optional<Author> findById(Long id);

    Author save(Author author);

    Boolean existsById(Long id);

    void delete(Author author);
}
