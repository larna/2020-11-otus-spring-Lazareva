package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Comment;

import java.util.List;

/**
 * Репозиторий для работы с комментариями
 */
public interface CommentRepository extends MongoRepository<Comment, String> {
    /**
     * Поиск по книге
     * @param bookId id выбранной книги
     * @return возвращает список комментариев
     */
    List<Comment> findAllByBook_Id(String bookId);

    /**
     * Удалить все комментарии для книги
     * @param bookId ид книги
     */
    void deleteAllByBook_Id(String bookId);
}
