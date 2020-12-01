package ru.otus.spring.service.testing;

import ru.otus.spring.domain.testing.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestions();
}
