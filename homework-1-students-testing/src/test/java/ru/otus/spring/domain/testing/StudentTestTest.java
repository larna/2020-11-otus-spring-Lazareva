package ru.otus.spring.domain.testing;

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

    @DisplayName("Корректно создает объект")
    @Test
    void shouldHaveCorrectConstructor() {
        Person student = new Person("Ivanov", 18);
        QuestionAnswer simpleAnswer1 = new QuestionAnswer("answer1", true);
        QuestionAnswer simpleAnswer2 = new QuestionAnswer("answer2", false);
        Question question = new Question("Question?", Arrays.asList(simpleAnswer1, simpleAnswer2));

        StudentTest test = new StudentTest(LocalDate.now(), student, Arrays.asList(question));
        assertAll(() -> assertNotNull(test),
                () -> assertEquals(LocalDate.now(), test.getTestDate()),
                () -> assertEquals(new Person("Ivanov", 18), test.getStudent()),
                () -> assertThat(test.getQuestions())
                        .hasSize(1)
                        .contains(new Question("Question?",Arrays.asList(simpleAnswer1, simpleAnswer2))),
                () -> assertNotNull(test.getAnswers()),
                () -> assertThat(test.getAnswers()).hasSize(0));
    }

    @DisplayName("Корректно сравнивает объекты")
    @Test
    void shouldHaveCorrectEquals() {
        Person student = new Person("Ivanov", 18);
        QuestionAnswer simpleAnswer1 = new QuestionAnswer("answer1", true);
        QuestionAnswer simpleAnswer2 = new QuestionAnswer("answer2", false);
        Question question = new Question("Question?", Arrays.asList(simpleAnswer1, simpleAnswer2));

        //для поиска теста ключевыми полями является человек и дата, поэтому список вопросов неважен
        StudentTest test = new StudentTest(LocalDate.now(), student, Arrays.asList(question));
        StudentTest testQuestionListEmpty = new StudentTest(LocalDate.now(), student, new ArrayList<>());
        StudentTest sameTest = new StudentTest(LocalDate.now(), student, Arrays.asList(question));
        StudentTest testOtherDate = new StudentTest(LocalDate.now().minus(1, ChronoUnit.DAYS), student, Arrays.asList(question));
        StudentTest testOtherStudent = new StudentTest(LocalDate.now(), new Person("Petrov", 19), Arrays.asList(question));

        assertAll(() -> assertTrue(test.equals(testQuestionListEmpty)),
                () -> assertTrue(test.equals(sameTest)),
                () -> assertFalse(test.equals(testOtherDate)),
                () -> assertFalse(test.equals(testOtherStudent)));
    }
}