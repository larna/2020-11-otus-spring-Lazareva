package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Author;

import java.util.List;

/**
 * Репозиторий для работы с авторами
 */
public interface AuthorRepository extends MongoRepository<Author, String> {
    List<Author> findAllByIdIn(List<String> authorsIdList);
}
