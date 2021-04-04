package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Author;

/**
 * Репозиторий для работы с авторами
 */
public interface AuthorRepository extends MongoRepository<Author, String> {
}
