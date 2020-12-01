package ru.otus.spring.domain.testing;

import lombok.Getter;

import java.util.Objects;

/**
 * Класс описывающий ответы на вопросы с указанием правильный/неправильный.
 */
public class QuestionAnswer extends SimpleAnswer implements Answer {
    /**
     * Маркер является ли ответ правильным
     */
    @Getter
    private final Boolean isRightAnswer;

    public QuestionAnswer(String answer, Boolean isRightAnswer) {
        super(answer);
        this.isRightAnswer = isRightAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        QuestionAnswer that = (QuestionAnswer) o;

        return Objects.equals(isRightAnswer, that.isRightAnswer);
    }

    @Override
    public String toString() {
        return "QuestionAnswer{" +
                "isRightAnswer=" + isRightAnswer +
                '}';
    }
}
