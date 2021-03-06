package ru.otus.spring.util.testing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.Question;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс QuestionParser")
class QuestionParserImplStudentTest {
    private static final String QUESTION = "Question?";
    private static final String ANSWER1 = "answer1";
    private static final String ANSWER2 = "answer2";

    private final QuestionParser parser = new QuestionParserImpl();

    private static Question defaultQuestion;


    @BeforeAll
    static void init() {
        final List<Answer> answers = new ArrayList<>();
        answers.add(new Answer(ANSWER1, true));
        answers.add(new Answer(ANSWER2, false));

        defaultQuestion = new Question(QUESTION, answers);
    }

    @DisplayName("Корректно разбирает строку о вопросе в формате csv")
    @Test
    void shouldHaveCorrectParse() throws Exception {
        final String correctString = String.format("%s, %s, true, %s, false ", QUESTION, ANSWER1, ANSWER2);
        InputStream in = new ByteArrayInputStream(correctString.getBytes(StandardCharsets.UTF_8));
        assertThat(parser.parse(in))
                .isNotEmpty()
                .hasSize(1)
                .contains(defaultQuestion);
    }

    @DisplayName("Корректно разбирает строку о вопросе (boolean значение в верхнем регистре)")
    @Test
    void shouldHaveCorrectParseUpperCaseBooleanValueString() {
        final String correctString = String.format("%s, %s, TRUE, %s, FALSE ", QUESTION, ANSWER1, ANSWER2);
        InputStream in = new ByteArrayInputStream(correctString.getBytes(StandardCharsets.UTF_8));
        assertAll(() -> assertEquals(parser.parse(in), Arrays.asList(defaultQuestion)));
    }

    @DisplayName("Корректно определяет неправильное количество ответов на вопрос")
    @Test
    void shouldHaveWrongCountAnswer() {
        final String wrongNumberOfAnswerString = String.format("%s, %s, true, %s, false, answer3 ", QUESTION, ANSWER1, ANSWER2);
        InputStream in = new ByteArrayInputStream(wrongNumberOfAnswerString.getBytes(StandardCharsets.UTF_8));
        Exception exception = assertThrows(QuestionFormatException.class,
                () -> parser.parse(in));
        assertTrue(exception.getMessage().contains("Wrong question format"));
    }

    @DisplayName("Корректно определяет неправильный формат ответов (для поля правильный ответ могут быть использованы только true или false)")
    @Test
    void shouldHaveWrongAnswerFormat() {
        final String wrongAnswerFormatString = String.format("%s, %s, %s ", QUESTION, ANSWER1, ANSWER2);
        InputStream in = new ByteArrayInputStream(wrongAnswerFormatString.getBytes(StandardCharsets.UTF_8));
        Exception exception = assertThrows(QuestionFormatException.class,
                () -> parser.parse(in));
        assertTrue(exception.getMessage().contains("Wrong question format"));
    }

    @DisplayName("Выбрасывает исключение, если строка csv имеет неправильный формат")
    @Test
    void shouldThrowQuestionFormatException() {
        InputStream in = new ByteArrayInputStream("wrong format".getBytes(StandardCharsets.UTF_8));
        Exception exception = assertThrows(QuestionFormatException.class,
                () -> parser.parse(in));
        assertTrue(exception.getMessage().contains("Wrong question format"));
    }
}