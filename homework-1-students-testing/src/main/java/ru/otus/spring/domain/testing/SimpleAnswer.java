package ru.otus.spring.domain.testing;

import lombok.Getter;

import java.util.Objects;

/**
 * Класс описывает ответ на вопрос, получаемый от студента
 */
public class SimpleAnswer implements Answer {
    /**
     * Собственно сам ответ на вопрос
     */
    @Getter
    private final String answer;

    public SimpleAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleAnswer simpleAnswer1 = (SimpleAnswer) o;

        return Objects.equals(answer, simpleAnswer1.answer);
    }

    @Override
    public String toString() {
        return "SimpleAnswer{" +
                "answer='" + answer + '\'' +
                '}';
    }
}
