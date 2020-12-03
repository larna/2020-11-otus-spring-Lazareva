package ru.otus.spring.domain.testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Question")
class QuestionTest {

    @DisplayName("Корректно создает объект")
    @Test
    void shouldHaveCorrectConstructor() {
        Question question = getQuestionObject();

        assertAll(()->assertNotNull(question),
                () -> assertEquals(2, question.getAnswers().size()),
                () -> assertEquals("Question?", question.getQuestion()),
                () -> assertEquals("answer1", question.getAnswers().get(0).getAnswer()),
                () -> assertEquals(true, ((QuestionAnswer)question.getAnswers().get(0)).getIsRightAnswer()),
                () -> assertEquals("answer2", question.getAnswers().get(1).getAnswer()),
                () -> assertEquals(false, ((QuestionAnswer)question.getAnswers().get(1)).getIsRightAnswer()));
    }

    @DisplayName("Корректно сравнивает объекты")
    @Test
    void shouldHaveCorrectEquals() {
        Question question1 = getQuestionObject();
        Question question2 = getQuestionObject();
        assertTrue(question1.equals(question2));
    }

    private Question getQuestionObject() {
        QuestionAnswer answer1 = new QuestionAnswer("answer1", true);
        QuestionAnswer answer2 = new QuestionAnswer("answer2", false);
        Question question = new Question("Question?", Arrays.asList(answer1, answer2));
        return question;
    }
}