package ru.otus.spring.service.testing;

import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.Question;

import java.util.List;

/**
 * Сервис вопросов
 */
public interface QuestionService {
    /**
     * Получить все вопросы
     *
     * @return список всех вопросов
     */
    List<Question> getQuestions();

}
