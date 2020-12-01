package ru.otus.spring.service.testing;

import ru.otus.spring.dao.testing.QuestionDao;
import ru.otus.spring.domain.testing.Question;

import java.util.List;

/**
 * Сервис вопросов
 */
public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao dao;

    public QuestionServiceImpl(QuestionDao dao) {
        this.dao = dao;
    }

    /**
     * Получить все вопросы
     *
     * @return список всех вопросов
     */
    @Override
    public List<Question> getQuestions() {
        return dao.findAll();
    }
}
