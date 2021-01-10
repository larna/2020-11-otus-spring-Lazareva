package ru.otus.spring.domain.testing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Question")
class QuestionTest {
    private static Question defaultQuestion;

    @BeforeAll
    static void init(){
        Answer answer1 = new Answer("right answer", true);
        Answer answer2 = new Answer("wrong answer", false);
        defaultQuestion = new Question("Question?", Arrays.asList(answer1, answer2));
    }
    @DisplayName("Корректно создает объект")
    @Test
    void shouldHaveCorrectConstructor() {
        Question question = defaultQuestion;

        assertAll(() -> assertNotNull(question),
                () -> assertEquals(2, question.getAnswers().size()),
                () -> assertEquals("Question?", question.getQuestion()),
                () -> assertEquals("right answer", question.getAnswers().get(0).getAnswer()),
                () -> assertEquals(true, ((Answer) question.getAnswers().get(0)).getIsRightAnswer()),
                () -> assertEquals("wrong answer", question.getAnswers().get(1).getAnswer()),
                () -> assertEquals(false, ((Answer) question.getAnswers().get(1)).getIsRightAnswer()));
    }

    @DisplayName("Корректно сравнивает объекты")
    @Test
    void shouldHaveCorrectEquals() {
        Question question1 = defaultQuestion;

        Answer answer1 = new Answer("right answer", true);
        Answer answer2 = new Answer("wrong answer", false);
        Question question2 = new Question("Question?", Arrays.asList(answer1, answer2));

        assertTrue(question1.equals(question2));
    }

    @DisplayName("Корректно проверяет правильность ответа")
    @Test
    void shouldHaveCorrectCheckAnswer() {
        Question question = defaultQuestion;
        assertAll(() -> assertTrue(question.checkAnswer("right answer")),
                () -> assertFalse(question.checkAnswer("wrong answer")));
    }

    @DisplayName("Корректно возвращает ответы на вопрос")
    @Test
    void shouldHaveCorrectGetAnswers() {
        Question question = defaultQuestion;
        assertThat(question.getAnswers())
                .isNotEmpty()
                .hasSize(2)
                .contains(new Answer("right answer", true))
                .contains(new Answer("wrong answer", false));
    }
    @DisplayName("Корректно возвращает ответ по номеру")
    @Test
    void shouldHaveCorrectGetAnswerByNumber() {
        Question question = defaultQuestion;
        assertEquals(new Answer("right answer", true), question.getAnswerByNumber(1));
    }

    @DisplayName("Выбрасывает исключение, если для указанного номера нет ответа")
    @Test
    void shouldThrowQuestionFormatException() {
        Question question = defaultQuestion;
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> question.getAnswerByNumber(4));
//        assertTrue(exception.getMessage().contains("Wrong question format"));
    }

    @DisplayName("Корректно возвращает список правильных ответов")
    @Test
    void shouldHaveCorrectGetRightAnswers() {
        Answer answer1 = new Answer("right answer", true);
        Answer answer2 = new Answer("right answer number 2", true);
        Answer answer3 = new Answer("wrong answer", false);
        Question question = new Question("Question?", Arrays.asList(answer1, answer2, answer3));

        assertThat(question.getRightAnswers())
                .isNotEmpty()
                .hasSize(2)
                .contains(new Answer("right answer", true))
                .contains(new Answer("right answer number 2", true));
    }

}