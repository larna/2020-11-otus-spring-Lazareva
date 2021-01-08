package ru.otus.spring.domain.testing;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * Класс описывающий ответы на вопросы с указанием правильный/неправильный.
 */
@EqualsAndHashCode(exclude = {"isRightAnswer"})
@ToString
@Getter
@NonNull
public class Answer {
    /**
     * ответ на вопрос
     */
    private final String answer;
    /**
     * Маркер является ли ответ правильным
     */
    private final Boolean isRightAnswer;

    public Answer(@NonNull String answer, @NonNull Boolean isRightAnswer) {
        this.answer = answer;
        this.isRightAnswer = isRightAnswer;
    }
}
