package ru.otus.spring.domain.testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Answer")
class AnswerTest {

    @DisplayName("Корректно создает объект")
    @Test
    void shouldHaveCorrectConstructor() {
        Answer answer = new Answer("answer", true);
        assertAll(()->assertNotNull(answer),
                ()->assertEquals("answer", answer.getAnswer()));
    }
    @DisplayName("Корректно сравнивает объекты")
    @Test
    void shouldHaveCorrectEquals() {
        Answer answer1 = new Answer("answer", true);
        Answer answer2 = new Answer("answer", true);
        assertTrue(answer1.equals(answer2));
    }
}