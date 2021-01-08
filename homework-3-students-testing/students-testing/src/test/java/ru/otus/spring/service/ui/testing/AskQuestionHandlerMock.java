package ru.otus.spring.service.ui.testing;

import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.service.ui.testing.question.AskQuestionHandler;
import ru.otus.spring.service.ui.testing.question.InputAnswerException;

public class AskQuestionHandlerMock implements AskQuestionHandler {
    @Override
    public Answer askQuestion(Integer counter, Question question) throws InputAnswerException {
        return new Answer("Eureka!", true);
    }

    @Override
    public Boolean isSuitableQuestion(Question question) {
        return true;
    }
}
