package ru.otus.spring.domain.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.domain.Person;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс StudentTest")
class StudentTestTest {
    private StudentTest test;
    private Answer simpleAnswer1;
    private Answer simpleAnswer2;
    private Person actualStudent;

    @BeforeEach
    void init() {
        actualStudent = new Person("Ivanov", "Ivan", 18);
        simpleAnswer1 = new Answer("answer1", true);
        simpleAnswer2 = new Answer("answer2", false);
        Question question = new Question("Question?", Arrays.asList(simpleAnswer1, simpleAnswer2));
        Question question2 = new Question("Question 2?", Arrays.asList(simpleAnswer1, simpleAnswer2));
        Question question3 = new Question("Question 3?", Arrays.asList(simpleAnswer1, simpleAnswer2));

        test = new StudentTest(LocalDate.now(), actualStudent, Arrays.asList(question, question2, question3));
    }

    @DisplayName("Корректно создает объект")
    @Test
    void shouldHaveCorrectConstructor() {
        Person expectedStudent = new Person("Ivanov", "Ivan", 18);
        Question expectedQuestion = new Question("Question?", Arrays.asList(simpleAnswer1, simpleAnswer2));
        LocalDate expectedDate = LocalDate.now();

        assertAll(() -> assertNotNull(test),
                () -> assertEquals(expectedDate, test.getTestDate()),
                () -> assertEquals(expectedStudent, test.getStudent()),
                () -> assertThat(test.getQuestions())
                        .hasSize(3)
                        .contains(expectedQuestion),
                () -> assertNotNull(test.getAnswers()),
                () -> assertThat(test.getAnswers()).hasSize(0));
    }

    @DisplayName("Корректно сравнивает объекты")
    @Test
    void shouldHaveCorrectEquals() {
        Question question = new Question("Question?", Arrays.asList(simpleAnswer1, simpleAnswer2));

        //для поиска теста ключевыми полями является человек и дата, поэтому список вопросов неважен
        StudentTest test = new StudentTest(LocalDate.now(), actualStudent, Arrays.asList(question));
        StudentTest testQuestionListEmpty = new StudentTest(LocalDate.now(), actualStudent, new ArrayList<>());
        StudentTest sameTest = new StudentTest(LocalDate.now(), actualStudent, Arrays.asList(question));
        StudentTest testOtherDate = new StudentTest(LocalDate.now().minus(1, ChronoUnit.DAYS), actualStudent, Arrays.asList(question));
        StudentTest testOtherStudent = new StudentTest(LocalDate.now(), new Person("Petrov", "Peter", 19), Arrays.asList(question));

        assertAll(() -> assertTrue(test.equals(testQuestionListEmpty)),
                () -> assertTrue(test.equals(sameTest)),
                () -> assertFalse(test.equals(testOtherDate)),
                () -> assertFalse(test.equals(testOtherStudent)));
    }

    @DisplayName("Корректно добавляет в объект ответ студента")
    @Test
    void shouldHaveCorrectAddAnswer() {
        Answer expectedAnswer1 = new Answer("answer1", true);

        test.addAnswer(simpleAnswer1);
        assertThat(test.getAnswers())
                .isNotEmpty()
                .hasSize(1)
                .contains(expectedAnswer1);
    }

    @DisplayName("Корректно определяет когда тест завершен")
    @Test
    void shouldHaveCorrectIsComplete() {
        assertFalse(test.isComplete());

        test.addAnswer(simpleAnswer1);
        test.addAnswer(simpleAnswer1);
        test.addAnswer(simpleAnswer1);

        assertTrue(test.isComplete());
    }

    @Test
    @DisplayName("Корректно считает процент выполнения теста")
    void shouldHaveCorrectGetFactPercentPassTest() {

        test.addAnswer(simpleAnswer1);
        test.addAnswer(simpleAnswer1);
        test.addAnswer(simpleAnswer2);

        Integer expected = 67;
        Integer actual = test.getFactPercentPassTest();
        assertEquals(expected, actual);
    }

}