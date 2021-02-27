package ru.otus.spring.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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

    Page<Author> findAll(Pageable pageable);

    Page<Author> findAll(AuthorSearchSpecification spec, Pageable pageable);

    Optional<Author> findById(Long id);

    Author save(Author author);

    Boolean existsById(Long id);

    void deleteById(Long id);
}
