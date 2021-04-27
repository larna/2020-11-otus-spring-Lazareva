package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Book;

/**
 * Репозитории для работы с книгами
 */
public interface BookRepository extends MongoRepository<Book, String>, BookCustomRepository {
}
