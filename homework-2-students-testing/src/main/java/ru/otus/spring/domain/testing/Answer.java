package ru.otus.spring.domain.testing;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Objects;

/**
 * Класс описывающий ответы на вопросы с указанием правильный/неправильный.
 */
@EqualsAndHashCode(exclude = {"isRightAnswer"})
@ToString
public class Answer {
    /**
     * ответ на вопрос
     */
    @Getter
    @NonNull
    private final String answer;
    /**
     * Маркер является ли ответ правильным
     */
    @Getter
    @NonNull
    private final Boolean isRightAnswer;

    public Answer(@NonNull String answer, @NonNull Boolean isRightAnswer) {
        this.answer = answer;
        this.isRightAnswer = isRightAnswer;
    }
}
