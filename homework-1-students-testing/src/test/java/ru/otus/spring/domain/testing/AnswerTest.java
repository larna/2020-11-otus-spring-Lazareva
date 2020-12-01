package ru.otus.spring.domain.testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Answer")
class AnswerTest {

    @DisplayName("Корректно создает объект")
    @Test
    void shouldHaveCorrectConstructor() {
        QuestionAnswer answer = new QuestionAnswer("answer", true);
        assertAll(()->assertNotNull(answer),
                ()->assertEquals("answer", answer.getAnswer()));
    }
    @DisplayName("Корректно сравнивает объекты")
    @Test
    void shouldHaveCorrectEquals() {
        QuestionAnswer answer1 = new QuestionAnswer("answer", true);
        QuestionAnswer answer2 = new QuestionAnswer("answer", true);
        assertTrue(answer1.equals(answer2));
    }
}