package ru.otus.spring.service.ui.testing.question;

import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.service.io.IOService;

/**
 * Базовый сервис опроса студента
 */
public abstract class BaseAskQuestionHandler implements AskQuestionHandler {
    private static final String QUESTION_TEMPLATE = "%d. %s";
    private final String answerInviteMessage;
    protected final IOService ioService;
    protected final LocalizationService localizationService;

    public BaseAskQuestionHandler(IOService ioService,
                                  LocalizationService localizationService,
                                  String answerInviteMessage) {
        this.ioService = ioService;
        this.localizationService = localizationService;
        this.answerInviteMessage = answerInviteMessage;
    }

    /**
     * Вынесла общий функционал по выводу вопроса и приветствия к вводу ответа
     *
     * @param counter
     * @param question
     */
    protected void printQuestion(Integer counter, Question question) {
        ioService.outMessage(String.format(QUESTION_TEMPLATE, counter, question.getQuestion()));
        ioService.outMessage(localizationService.getMessage(answerInviteMessage));
    }
}
