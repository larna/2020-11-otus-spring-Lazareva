package ru.otus.spring.config.props;

import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.results.Header;
import ru.otus.spring.service.i18n.LocalizationService;

import java.time.format.DateTimeFormatter;

/**
 * Настройка
 */
@Component
@ConfigurationProperties(prefix = "application.test.result")
public class TestResultProps extends BaseMessagesProps {
    private static final String HEADER_DEFAULT = "\t Test result";
    private static final String DONE_DEFAULT = "\t Test done";
    private static final String FAILED_DEFAULT = "\t Test failed";
    private static final String ANSWER_DEFAULT = " Your answer: ";
    private static final String ANSWER_RIGHT_DEFAULT = " Answer is right";
    private static final String ANSWER_WRONG_DEFAULT = " Answer is wrong";
    private static final String RIGHT_ANSWERS_DEFAULT = "Right answers: ";
    private static final String RIGHT_ANSWERS_NOT_DEFINED_DEFAULT = "Right answers not define in question!";

    TestResultProps(LocalizationService localizationService) {
        super(localizationService);
    }

    public String getHeaderMessage(@NonNull Header header) {
        String studentName = header.getPerson().getFullName();
        String dateTest = header.getDate().format(DateTimeFormatter.ISO_DATE);
        return getMessage("header", HEADER_DEFAULT, studentName, dateTest);
    }

    public String getStatusTestMessage(Boolean isFailed) {
        String messageName = isFailed ? "failed" : "done";
        String defaultMessage = isFailed ? FAILED_DEFAULT : DONE_DEFAULT;
        return getMessage(messageName, defaultMessage);
    }

    public String getAnswerMessage(Answer answer) {
        String defaultAnswer = ANSWER_DEFAULT + answer.getAnswer();
        String defaultCorrectness = answer.getIsRightAnswer() ? ANSWER_RIGHT_DEFAULT : ANSWER_WRONG_DEFAULT;

        String userAnswer = getMessage("answer", defaultAnswer, answer.getAnswer());
        String correctness = getMessage(answer.getIsRightAnswer() ? "answerRight" : "answerWrong", defaultCorrectness);

        return userAnswer + "\n" + correctness;
    }

    public String getRightAnswersMessage(String rightAnswers) {
        String defaultMessage = RIGHT_ANSWERS_DEFAULT + rightAnswers;
        return getMessage("rightAnswersCount", defaultMessage, rightAnswers);
    }

    public String getNotDefinedRightAnswersMessage() {
        return getMessage("rightAnswersNotDefined", RIGHT_ANSWERS_NOT_DEFINED_DEFAULT);
    }
}
