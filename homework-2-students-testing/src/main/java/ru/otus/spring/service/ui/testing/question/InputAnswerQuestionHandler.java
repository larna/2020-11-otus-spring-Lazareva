package ru.otus.spring.service.ui.testing.question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.service.io.IOService;

import java.io.IOException;
/**
 * Сервис обрабатывает вопросы на которые нужно вводить ответы
 */
@Component
public class InputAnswerQuestionHandler extends BaseAskQuestionHandler {
    private static final Logger logger = LoggerFactory.getLogger(InputAnswerQuestionHandler.class);
    private static final String INPUT_ANSWER_MESSAGE = "\tEnter your answer:";

    @Autowired
    public InputAnswerQuestionHandler(IOService ioService) {
        super(ioService, INPUT_ANSWER_MESSAGE);
    }

    @Override
    public Answer askQuestion(Integer counter, Question question) throws InputAnswerException {
        super.printQuestion(counter, question);

        try {
            String inputAnswer = ioService.readMessage();
            Boolean isRight = question.checkAnswer(inputAnswer);
            return new Answer(inputAnswer, isRight);

        } catch (IOException e) {
            logger.error("IO error for question - " + question, e);
            throw new InputAnswerException(e);
        }

    }

    @Override
    public Boolean isSuitableQuestion(Question question) {
        long wrongAnswerCount = question.getAnswers().stream().filter(answer -> !answer.getIsRightAnswer()).count();
        return wrongAnswerCount == 0;
    }
}
