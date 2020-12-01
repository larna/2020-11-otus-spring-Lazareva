package ru.otus.spring.dao.testing;

import ru.otus.spring.domain.testing.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
