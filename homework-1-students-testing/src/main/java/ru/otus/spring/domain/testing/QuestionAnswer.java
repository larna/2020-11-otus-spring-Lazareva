package ru.otus.spring.domain.testing;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Objects;

/**
 * Класс описывающий ответы на вопросы с указанием правильный/неправильный.
 */
@EqualsAndHashCode(callSuper = true)
@ToString
public class QuestionAnswer extends SimpleAnswer implements Answer {
    /**
     * Маркер является ли ответ правильным
     */
    @Getter
    @NonNull
    private final Boolean isRightAnswer;

    public QuestionAnswer(String answer, Boolean isRightAnswer) {
        super(answer);
        this.isRightAnswer = isRightAnswer;
    }
}
