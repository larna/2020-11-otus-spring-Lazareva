package ru.otus.spring.service.ui.testing.question;

import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.Question;

/**
 * Интерфейс для обработки ввода/вывода вопросов
 */
public interface AskQuestionHandler {
    /**
     * Опрос студента (задает один вопрос и получает ответ)
     * @param counter
     * @param question
     * @return
     * @throws InputAnswerException
     */
    Answer askQuestion(Integer counter, Question question) throws InputAnswerException;
    /**
     * Проверить возможно ли обработать вопрос и получить ответ от студента с помощью этого сервиса
     */
    Boolean isSuitableQuestion(Question question);

}
