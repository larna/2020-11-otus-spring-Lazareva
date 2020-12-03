package ru.otus.spring.domain.testing;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Objects;

/**
 * Класс описывает ответ на вопрос, получаемый от студента
 */
@EqualsAndHashCode
@ToString
public class SimpleAnswer implements Answer {
    /**
     * Собственно сам ответ на вопрос
     */
    @Getter
    @NonNull
    private final String answer;

    public SimpleAnswer(String answer) {
        this.answer = answer;
    }

}
