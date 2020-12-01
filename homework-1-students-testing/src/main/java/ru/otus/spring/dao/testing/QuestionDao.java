package ru.otus.spring.dao.testing;

import ru.otus.spring.domain.testing.Question;

import java.util.List;

/**
 * Класс для доступа к объектам Question
 */
public interface QuestionDao {
    /**
     * Метод findAll перечитывает файл каждый раз когда к нему обращаются
     * Вдруг за прошедшее время с последнего обращения файлик поменяли и там другие вопросы или ответы:)
     *
     * @return список вопросов из ресурса
     */
    List<Question> findAll();
}
