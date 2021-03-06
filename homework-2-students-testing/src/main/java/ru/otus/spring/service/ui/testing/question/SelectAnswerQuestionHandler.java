package ru.otus.spring.service.ui.testing.question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.service.io.IOService;

import java.io.IOException;
import java.util.stream.IntStream;
/**
 * Сервис обрабатывает вопросы для которых нужно выбирать ответы
 */
@Component
public class SelectAnswerQuestionHandler extends BaseAskQuestionHandler {
    private static final Logger logger = LoggerFactory.getLogger(InputAnswerQuestionHandler.class);

    private static final String SELECT_ANSWER_MESSAGE = "\tEnter number of choice answer:";
    private static final String ANSWER_INVITE_REPEAT_SELECT_MESSAGE = "The answered number was not found. Repeat enter, please.";

    @Autowired
    public SelectAnswerQuestionHandler(IOService ioService) {
        super(ioService, SELECT_ANSWER_MESSAGE);
    }

    @Override
    public Answer askQuestion(Integer counter, Question question) throws InputAnswerException {
        super.printQuestion(counter, question);
        ioService.outMessage(formatAnswersForQuestion(question));

        try {
            String inputAnswer = ioService.readMessage();
            int number = Integer.parseInt(inputAnswer);
            return question.getAnswerByNumber(number);

        } catch (IOException e) {
            logger.error("IO error for question - " + question, e);
            throw new InputAnswerException(e);

        } catch (IllegalArgumentException e) {
            logger.info(ANSWER_INVITE_REPEAT_SELECT_MESSAGE + question);
            ioService.outMessage(ANSWER_INVITE_REPEAT_SELECT_MESSAGE);
            return askQuestion(counter, question);
        }
    }

    @Override
    public Boolean isSuitableQuestion(Question question) {
        long wrongAnswerCount = question.getAnswers().stream().filter(answer -> !answer.getIsRightAnswer()).count();
        return wrongAnswerCount > 0;
    }

    /**
     * Сформировать строку для выбора ответа на вопрос
     *
     * @return ответы в виде строки с указанием порядкового номера, который будет выбирать пользователь
     */
    private String formatAnswersForQuestion(Question question) {
        if (question.getAnswers().isEmpty())
            return "";
        return IntStream.range(0, question.getAnswers().size())
                .mapToObj(i -> String.format("\t%d. %s", i + 1, question.getAnswers().get(i).getAnswer()))
                .reduce((s1, s2) -> s1 + " " + s2).orElse("");
    }
}
